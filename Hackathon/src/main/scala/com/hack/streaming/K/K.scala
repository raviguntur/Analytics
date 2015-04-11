package com.hack.streaming

import com.hack.streaming.config.RabbitConfig

object KON {

  val DATA_SOURCE_RABBIT = "DSRABBIT"
  var rabbitConfig = new RabbitConfig("127.0.0.1", 5672)
}