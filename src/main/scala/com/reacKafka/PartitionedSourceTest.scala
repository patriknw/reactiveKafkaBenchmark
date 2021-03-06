package com.reacKafka

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.kafka.ConsumerMessage.CommittableOffsetBatch
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerMessage, ConsumerSettings, Subscriptions}
import akka.pattern.{ask, gracefulStop}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.reacKafka.utils.{Msg, Receiver}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by admin on 03/01/17.
  */
object PartitionedSourceTest extends App {
  implicit val system = ActorSystem()

  val receiver = system.actorOf(Props(classOf[Receiver]))

  val consumerSettings = ConsumerSettings(system.settings.config.getConfig("kafka.kafka-clients"), new StringDeserializer, new StringDeserializer)
    .withBootstrapServers("192.168.100.100:9092")
    .withGroupId("ReactiveConsumer")

  implicit val executionContext = system.dispatchers.lookup("akka.actor.default-dispatcher")
  val kafkaExecutionContext = system.dispatchers.lookup("akka.kafka.default-dispatcher")

  val topic = Set(system.settings.config.getString("topicName"))

  val stream = Consumer.committablePartitionedSource[String, String](consumerSettings, Subscriptions.topics(topic))
    .mapAsyncUnordered(100) {
      case (topic, partitionStream) =>
        partitionStream
          .mapAsync(1000) {
            s: ConsumerMessage.CommittableMessage[String, String] =>
              val timeStamp = s.record.value().toLong
              val msg = Msg(timeStamp, s.record.topic(), s.record.partition().toString)
              send(msg).map(e => s.committableOffset)
          }
          .groupedWithin(500, 1.seconds)
          .map(group => group.foldLeft(CommittableOffsetBatch.empty) { (batch, elem) => batch.updated(elem) })
          .async.map(_.commitScaladsl())
          .runWith(Sink.ignore)(ActorMaterializer.create(system))
    }.runWith(Sink.ignore)(ActorMaterializer.create(system))

  //
  def send(msg: Msg) = ask(receiver, msg)(1 seconds)

  scala.sys.addShutdownHook {
    val supervisorStop = gracefulStop(receiver, 20 seconds, PoisonPill)
    Await.result(supervisorStop, 30 seconds)
    system.terminate()
    Await.result(system.whenTerminated, 30 seconds)
  }
}




