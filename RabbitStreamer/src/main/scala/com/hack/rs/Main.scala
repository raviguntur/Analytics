package com.hack.rs

import scala.io.Source

import com.rabbitmq.client.ConnectionFactory

object Main {

  def main(args: Array[String]) {

    for (line <- Source.fromFile(args(0)).getLines()) {
      val x = line.getBytes
      println(line)
      var factory = new ConnectionFactory()
      factory.setHost("127.0.0.1")
      factory.setPort(5672)
      factory.setUsername("guest")
      factory.setPassword("guest")

      val connection = factory.newConnection()
      val channel = connection.createChannel()
      channel.basicPublish("", "com.hack.queue", null, x);
      Thread.sleep(2000)

      connection.close();

    }
  }
}
