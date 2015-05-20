package com.hack.streaming.Main

import spark.jobserver.SparkJob
import spark.jobserver.SparkJobValid
import org.apache.spark.SparkContext
import spark.jobserver.SparkJobInvalid
import spark.jobserver.SparkJobValidation
import spark.jobserver.NamedRddSupport
import scala.util.Try
import com.typesafe.config.Config

import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.rdd.PairRDDFunctions

object ReadHotelData extends SparkJob with NamedRddSupport {
  override def validate(sc: SparkContext, config: Config): SparkJobValidation = {
    Try(config.getString("init.getdata"))
      .map(x => SparkJobValid)
      .getOrElse(SparkJobInvalid("No input.string config param"))

    val rdd = this.namedRdds.get[Models.StreamedData]("thedata")

    if (rdd.isDefined) SparkJobValid else SparkJobInvalid("Missing named RDD [dbpedia_dict]")
  }

  override def runJob(sc: SparkContext, config: Config): Any = {
    config.getString("init.getdata") match {
      case "FREQUENT_SEARCHES" => get_frequentSearches(sc)
      case "FREQUENT_PAGES" => get_frequentPages(sc)
    }
  }

  def get_frequentSearches(sc: SparkContext): Any = {
    val dataRDD = this.namedRdds.get[String]("thedata").get
    val dataModel = dataRDD.map { f => TransformStreamedData.fromString(f) }
    var counts = 1L

    if (dataModel.count() > 0) counts = dataModel.count()

    dataModel.filter(f => f != null).map(f => (f.phrase, 1)).
      reduceByKey((a, b) => a + b).map(f => (f._1, f._2)).sortBy(_._2, false, 2).collect().toList
  }

  def get_frequentPages(sc: SparkContext): Any = {
    val dataRDD = this.namedRdds.get[String]("thedata").get
    val dataModel = dataRDD.map { f => TransformStreamedData.fromString(f) }
    var counts = 1L

    if (dataModel.count() > 0) counts = dataModel.count()

    dataModel.filter(f => f != null).map(f => (f.pageAccessed, 1)).
      reduceByKey((a, b) => a + b).map(f => (f._1, 100 * f._2 / counts)).sortBy(_._2, false, 2).collect().toList
  }
}