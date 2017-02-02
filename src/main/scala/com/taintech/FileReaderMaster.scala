package com.taintech

import java.io.FileReader

import akka.actor.{Actor, ActorLogging, Props}
import Slave.{EndOfFile, Line}
import au.com.bytecode.opencsv.{CSVParser, CSVReader}

import scala.io.Source

class FileReaderMaster extends Actor with ActorLogging {

  val csv = new CSVParser(',')
  val lines: Iterator[String] = Source.fromFile("/Users/taintech/Downloads/amazon-fine-foods/Reviews.csv").getLines

  def next = csv.parseLine(lines.next())

  override def preStart(): Unit = {
    // create the greeter actor
    val slave = context.actorOf(Props[Slave], "greeter")
    lines.next()
    slave ! next
  }

  def receive = {
    case Slave.NextPlease => sender() ! next
    // when the greeter is done, stop this actor and with it the application
    case Slave.Done => context.stop(self)
  }
}

