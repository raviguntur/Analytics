package com.hack.streaming.Main

import scala.collection.immutable.HashMap

object Models extends Serializable {

  class StreamedData(val time: String,
    val uid: String,
    val activity: String,
    val pageAccessed: String,
    val numberlong: String,
    val floatlong: String,
    val phrase: String,
    val last: String)

    extends Serializable {
    override def toString(): String = {
      "%s\t%s\n".format(phrase, uid)
    }
  }
}

object TransformStreamedData extends Serializable {
  def fromString(in: String): Models.StreamedData = {


    var parts = in.replace("\"", " ").split(",").toList
    println(parts)

    if (parts.length != 8)
      return null

    new Models.StreamedData(parts(0).trim(), parts(1).trim(), parts(2).trim(),
      parts(3).trim(), parts(4).trim(), parts(5).trim(), parts(6).trim(), parts(7).trim())

  }
  def tupleString(in: String) = {
    val parts = in.split(",")
    (parts(0), parts(1), parts(2), parts(3),
      parts(4).toLong, parts(5).toFloat, parts(6), parts(8))
  }
}

