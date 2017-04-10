package com.reacKafka

import akka.actor.ActorSystem
import com.reacKafka.utils.ApplicationConfig
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import scala.collection.JavaConversions._
import scala.util.Random

object RequestApp extends App {
  implicit lazy val system: ActorSystem = ActorSystem("RequestApp")

  val topic = system.settings.config.getString("topicName")
  val props = new scala.collection.mutable.HashMap[String, String]()
  props.put("bootstrap.servers", ApplicationConfig.kafka.producer.producer.getString("bootstrap.servers"))

  val r = new Random

  val producer = new KafkaProducer(props, new StringSerializer, new StringSerializer)
  while (true) {
    (1 to 25).foreach { y =>
      val time = System.currentTimeMillis().toString
      producer.send(new ProducerRecord[String, String](topic, r.nextInt(1000).toString, time))
    }
    Thread.sleep(1)
  }

  sys.addShutdownHook {
    system.terminate()
  }
}