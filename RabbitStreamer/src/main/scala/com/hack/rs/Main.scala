package com.hack.rs

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import scala.io.Source
import scala.collection.immutable.StringOps

object Main {

  def main(args: Array[String]) {

    for (line <- Source.fromFile(args(0)).getLines()) {
      val x = line.getBytes
      println(line)
      var factory = new ConnectionFactory()
      val connection = factory.newConnection()
      val channel = connection.createChannel();
      channel.queueDeclare("com.hack.queue", false, false, false, null);

      factory.setHost("127.0.0.1")
      factory.setPort(5672)
      factory.setUsername("guest")
      factory.setPassword("guest")
      Thread.sleep(1)
      channel.basicPublish("", "com.hack.queue", null, x);
      channel.close();
      connection.close();
    }

  }

}