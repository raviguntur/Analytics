package com.hack.streaming.Main

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream

import com.hack.streaming.KON
import com.hack.streaming.Main.Models.MessageParser
import com.hack.streaming.Main.Models.StreamedData

object Helper {

  def dataLoaderS(ssc: StreamingContext): DStream[StreamedData] = {
    println("In dataloaders")
    var events = new SetupSource(ssc, KON.DATA_SOURCE_RABBIT).getDataHandle()

    events.flatMap(new MessageParser(_).clean())
      .map(TransformStreamedData.fromString(_))
  }
}