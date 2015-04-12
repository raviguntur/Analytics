package com.hack.streaming.sources

import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.storage.StorageLevel.MEMORY_AND_DISK_2
import org.apache.spark.Logging
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import com.hack.streaming.config.NetworkConfig

class NetworkPort(netConfig: NetworkConfig)
  extends Receiver[String](MEMORY_AND_DISK_2) with Logging {

  def onStart() {
    new Thread("Network Port Receiver") {
      override def run() { receive() }
    }.start()
  }

  def onStop() {

  }

  private def receive() {
    var socket: Socket = null
    var userInput: String = null

    try {
      socket = new Socket(netConfig.host, netConfig.port)
      println("network receiver connection")
      // Until stopped or connection broken continue reading
      val reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"))
      userInput = reader.readLine()
      while (!isStopped && userInput != null) {
        store(userInput)
        userInput = reader.readLine()
      }
      reader.close()
      socket.close()

      // Restart in an attempt to connect again when server is active again
      restart("Trying to connect again")
    } catch {
      case e: java.net.ConnectException =>
        // restart if could not connect to server
        restart("Error connecting to " + netConfig.host + ":" + netConfig.port, e)
      case t: Throwable =>
        // restart if there is any other error
        restart("Error receiving data", t)
    }
  }
}