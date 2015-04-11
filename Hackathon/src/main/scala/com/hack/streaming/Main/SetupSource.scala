package com.hack.streaming.Main

import com.hack.streaming.KON.DATA_SOURCE_RABBIT
import com.hack.streaming.KON.rabbitConfig
import com.hack.streaming.sources.RabbitQueue
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream

class SetupSource(val ssc: StreamingContext, val algo: String) {
  println("SetupSource:Setting up source")
  var handle: ReceiverInputDStream[String] = null

  algo match {
    case DATA_SOURCE_RABBIT => {
      handle = ssc.receiverStream(new RabbitQueue(rabbitConfig))
      println("Handle is {}", handle)
    }
  }

  def getDataHandle(): ReceiverInputDStream[String] = {
    return handle
  }
}



