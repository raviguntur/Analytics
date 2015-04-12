package com.hack.streaming.KON

import com.hack.streaming.config.RabbitConfig
import com.hack.streaming.config.NetworkConfig
object KON {

  val DATA_SOURCE_RABBIT = "DSRABBIT"
  val DATA_SOURCE_NETWORK = "DSNETWORK"
  var rabbitConfig = new RabbitConfig("127.0.0.1", 5672)
  var netConfig = new NetworkConfig("127.0.0.1", 8888)

}