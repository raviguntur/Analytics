package com.hack.streaming.Main

import scala.collection.immutable.HashMap

object Models {

  class MessageParser(var data: String) {
    def clean(): Array[String] = {
      return data.split(":").filter(f => f.contains("-"))
    }

  }
  class StreamedData(val phrase: String, val course: String)
    extends Serializable {
    override def toString(): String = {
      "%s\t%s\n".format(phrase, course)
    }
  }
}

object TransformStreamedData extends Serializable {
  def fromString(in: String): Models.StreamedData = {
    val parts = in.split("-")
    new Models.StreamedData(parts(0), parts(1))
  }
}


