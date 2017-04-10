package com.reacKafka.utils

import akka.actor.Actor

import scala.concurrent.Future
import scala.util.Random
import akka.pattern.pipe

/**
  * Created by admin on 17/01/17.
  */
class Receiver extends Actor {
  val stat = new Stat
  val n = 10000
  var partitions = Set[String]()
  var lastPrint = System.currentTimeMillis()

  override def receive: Receive = {
    case msg@Msg(inputTime, topic, partition) =>
      partitions += partition

      val d = time() - inputTime

      stat.add(d.toInt)
      if (stat.count % n == 0) {
        val newTime = System.currentTimeMillis()

        println(stat.toString + " " + (n.toDouble / (newTime - lastPrint)) * 1000 ) //+ " " + partitions)
        partitions = Set()
        lastPrint = newTime
        stat.flush()
      }

      sender() ! Commit(msg.requestId)
  }

  def time() = System.currentTimeMillis()

  override def postStop(): Unit = {
    println(stat.toString)
    super.postStop()
  }
}
