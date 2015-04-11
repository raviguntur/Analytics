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
import com.hack.streaming.Main.Helper.dataLoaderS
import org.apache.spark.streaming.dstream.DStream
import com.hack.streaming.Main.Models.StreamedData
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import com.hack.streaming.KON
import com.hack.streaming.Main.Models.MessageParser

object LoadHotelData extends SparkJob with NamedRddSupport {

  val SPARK_DATA_PARTITIONS = 100
  var hotelRDD: DStream[StreamedData] = _
  var dataBlock: List[Models.StreamedData] = _
  var ssc: StreamingContext = _

  //  def main(args: Array[String]) {
  //    val sc = new SparkContext("local[4]", "StreamingJob")
  //    val results = runJob(sc, null)
  //    println("Result is " + results)
  //  }

  override def validate(sc: SparkContext, config: Config): SparkJobValidation = SparkJobValid

  override def runJob(sc: SparkContext, config: Config): Any = {
    if (Try(config.getString("init.string") == "INIT").isSuccess) {
      ssc = new StreamingContext(sc, Seconds(1))
      println("Received init message")
      var events = new SetupSource(ssc, KON.DATA_SOURCE_RABBIT).getDataHandle()

      hotelRDD = events.flatMap(new MessageParser(_).clean())
        .map(TransformStreamedData.fromString(_))

      hotelRDD.foreachRDD(rdd => { val x = rdd.collect().toList })
      ssc.start()
      ssc.awaitTermination()

    } else if (Try(config.getString("init.string") == "GETSTAT").isSuccess) {
      //      val counts = hotelRDD.map(q => (q.phrase, q.course)).countByValue()
      //
      //      //window of 60 secs and slide of 10 secs
      //      val res = counts.reduceByKeyAndWindow((a: Long, b: Long) => a + b, Seconds(1), Seconds(1))
      //      return res
    }

  }

}