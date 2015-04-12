package com.hack.streaming.config

class NetworkConfig(var host: String, var port: Int) extends Serializable {

  def setHost(h: String) { host = h }
  def setPort(p: Int) { port = p }

}