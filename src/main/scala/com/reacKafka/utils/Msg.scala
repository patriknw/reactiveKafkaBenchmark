package com.reacKafka.utils

import java.util.UUID

case class Msg(time: Long, topic: String, partition:String) {
  val requestId = UUID.randomUUID().toString
}


