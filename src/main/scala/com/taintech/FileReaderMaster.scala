package com.taintech

import akka.actor.{Actor, ActorLogging, Props}
import Slave.{EndOfFile, Line}

import scala.io.Source

class FileReaderMaster extends Actor with ActorLogging{

  override def preStart(): Unit = {
    // create the greeter actor
    val slave = context.actorOf(Props[Slave], "greeter")
    // tell it to perform the greeting

    val iterator = Source.fromFile("/Users/taintech/Downloads/amazon-fine-foods/Reviews.csv").getLines
    iterator.next()
    while(iterator.nonEmpty){
      slave ! Line(iterator.next)
    }
    slave ! EndOfFile
  }

  def receive = {
    // when the greeter is done, stop this actor and with it the application
    case Slave.Done => context.stop(self)
  }
}

