package com.hack.streaming.config

class RabbitConfig(var host: String, var port: Int) extends Serializable{
  var userName: String = "guest"
  var password: String = "guest"
  var resQueue: String = "com.hack.queue"
  var routingKey: String = "com.hack.queue"

  def setUserName(u: String) { userName = u }
  def setPassord(p: String) { password = p }
  def setResponseQueue(q: String) { resQueue = q }
}




