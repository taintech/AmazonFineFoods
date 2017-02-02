package com.taintech

import java.io.IOException

import akka.actor.{Actor, ActorLogging, Props}
import au.com.bytecode.opencsv.CSVParser
import com.taintech.Slave.{EndOfFile, FailedToParse}

import scala.io.Source

class FileReaderMaster extends Actor with ActorLogging {

  val csv = new CSVParser(',', '"', 0, false)
  val lines: Iterator[String] = Source.fromFile("/Users/taintech/Downloads/amazon-fine-foods/Reviews.csv").getLines


  override def preStart(): Unit = {
    // create the greeter actor
    val slave = context.actorOf(Props[Slave], "greeter")
    lines.next()
    slave ! lines.next()
  }

  def receive = {
    case Slave.NextPlease if lines.nonEmpty => {
      val line = lines.next()
      try {
        sender() ! csv.parseLine(line)
      } catch {
        case ex: IOException =>
          log.error(ex, "Failed to Parse")
          sender() ! (FailedToParse, line)
        case e: Any =>
          log.info("Something strange." + e.toString)
      }
    }
    case Slave.NextPlease if lines.isEmpty => sender() ! EndOfFile
    // when the greeter is done, stop this actor and with it the application
    case Slave.Done => context.stop(self)
  }
}

