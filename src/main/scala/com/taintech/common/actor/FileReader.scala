package com.taintech.common.actor

import akka.actor.{Actor, ActorLogging, Props}
import FileReader.{EOF, Next}

import scala.io.Source

/**
  * Author: Rinat Tainov 
  * Date: 03/02/2017
  */
class FileReader(filePath: String) extends Actor with ActorLogging {

  var iterator: Iterator[String] = _

  override def preStart(): Unit = iterator = Source.fromFile(filePath).getLines

  def receive = {
    case Next if iterator.nonEmpty => sender() ! iterator.next()
    case Next if iterator.isEmpty => sender() ! EOF
  }

}

object FileReader {

  case object Next

  case object EOF

  def props(filePath: String): Props = {
    require(filePath.nonEmpty)
    Props(new FileReader(filePath))
  }
}