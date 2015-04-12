package com.hack.streaming.Main

import com.hack.streaming.KON.KON.DATA_SOURCE_RABBIT
import com.hack.streaming.KON.KON.DATA_SOURCE_NETWORK
import com.hack.streaming.KON.KON.rabbitConfig
import com.hack.streaming.KON.KON.netConfig
import com.hack.streaming.config.NetworkConfig
import com.hack.streaming.sources.RabbitQueue
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import com.hack.streaming.sources.NetworkPort

class SetupSource(val ssc: StreamingContext, val algo: String) {
  println("SetupSource:Setting up source")
  var handle: ReceiverInputDStream[String] = null

  algo match {
    case DATA_SOURCE_RABBIT => handle = ssc.receiverStream(new RabbitQueue(rabbitConfig))
    case DATA_SOURCE_NETWORK => handle = ssc.receiverStream(new NetworkPort(netConfig))
  }

  def getDataHandle(): ReceiverInputDStream[String] = {
    return handle
  }
}



