package com.hack.streaming.Main

import spark.jobserver.SparkJob
import spark.jobserver.NamedRddSupport
import java.io.FileInputStream
import spark.jobserver.SparkJobValidation
import spark.jobserver.SparkJobValid
import org.apache.spark.SparkContext
import com.typesafe.config.Config
import scala.util.Try
import org.apache.spark.rdd.RDD
//import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream._
import com.hack.streaming.Main.Models.StreamedData
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import com.hack.streaming.KON.KON
import org.apache.spark.storage.StorageLevel.MEMORY_AND_DISK_2
import spark.jobserver.SparkJobInvalid

import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.rdd._
import org.apache.spark.SparkContext._

object LoadHotelData extends SparkJob with NamedRddSupport {

  val SPARK_DATA_PARTITIONS = 2
  val SPARK_INTERVAL = 1

  var hotelRDD: DStream[StreamedData] = _
  var dataBlock: RDD[String] = _
  var ssc: StreamingContext = _

  override def validate(sc: SparkContext, config: Config): SparkJobValidation = SparkJobValid

  override def runJob(sc: SparkContext, config: Config): Any = {

    if (Try(config.getString("init.string") == "INIT").isSuccess) {
      ssc = new StreamingContext(sc, Seconds(SPARK_INTERVAL))
      println("Received init message")

      var events = new SetupSource(ssc, KON.DATA_SOURCE_RABBIT).getDataHandle()
      var reduced = events.window(Seconds(100)).map { f => f.toLowerCase() }
      var windowData = sc.makeRDD(List("aaa", "bbb")).persist(MEMORY_AND_DISK_2)
      this.namedRdds.update("thedata", windowData)

      reduced.foreachRDD(rdd => {
        windowData = windowData ++ rdd
        this.namedRdds.update("thedata", windowData)
      })

      ssc.start()
      ssc.awaitTermination()
    }

  }

}