package com.hack.streaming.sources

import com.hack.streaming.config.RabbitConfig
import java.io.IOException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.storage.StorageLevel.MEMORY_AND_DISK_2
import org.apache.spark.Logging
import com.rabbitmq.client.QueueingConsumer
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.QueueingConsumer
import com.rabbitmq.client.ConnectionFactory

class RabbitQueue(rabbitConfig: RabbitConfig)
  extends Receiver[String](MEMORY_AND_DISK_2) with Logging {

  var factory: ConnectionFactory = null
  var connection: Connection = null
  val logger = LoggerFactory.getLogger(classOf[RabbitQueue])

  def onStart() {
    println("Called onStart ")
    new Thread("Network Port Receiver") {
      override def run() { println("Initiating the data run"); receive() }
    }.start()
  }

  def onStop() {

  }

  def getConnection(): Connection = {
    if (connection == null) {
      factory = new ConnectionFactory()
      factory.setHost(rabbitConfig.host)
      factory.setPort(rabbitConfig.port)
      factory.setUsername(rabbitConfig.userName)
      factory.setPassword(rabbitConfig.password)
      try {
        connection = factory.newConnection()
      } catch {
        case ex: Exception => {
          println("Error in setting up connection {}", ex)
          logger.error("Exception {}", ex)
          throw ex
        }
      }
    }
    return connection;
  }

  def shutdown() {
    if (connection != null) {
      try {
        connection.close();
      } catch {
        case ex: Exception => {
          logger.error("Exception {}", ex)
          throw ex
        }
      }
    }
  }

  def receive() {
    var message: String = null;
    var connection: Connection = getConnection();
    var channel: Channel = null;
    try {
      channel = connection.createChannel();
      var consumer = new QueueingConsumer(channel);

      channel.basicConsume(rabbitConfig.resQueue, true, consumer);
      println("Receiver.receiveMessage(): Waiting for message from response queue");

      var delivery = consumer.nextDelivery()
      var message = new String(delivery.getBody())

      while (!isStopped && message != null) {
        store(message)
        delivery = consumer.nextDelivery()
        message = new String(delivery.getBody());
        println("Message got is " + message)
      }
      println("Receiver.receiveMessage(): Got message from response queue");
      channel.close();
    } catch {

      case ex: Exception => {
        println("Failed:Receiver.recieveMessage(): Error in reading from response queue {}", ex);
        throw ex
      }

    }
  }

}