package com.taintech.aff.main

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props, Terminated}
import com.taintech.aff.actor.AmazonFineFoodManager

object AmazonFineFoodApp {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("amazon-fine-foods")
    val manager = system.actorOf(
      AmazonFineFoodManager.props(args.headOption.getOrElse("/Users/taintech/Downloads/amazon-fine-foods/Reviews.csv")), "manager"
    )
    system.actorOf(Props(classOf[Terminator], manager), "terminator")
  }

  class Terminator(ref: ActorRef) extends Actor with ActorLogging {
    context watch ref
    def receive = {
      case Terminated(_) =>
        log.info("{} has terminated, shutting down system", ref.path)
        context.system.terminate()
    }
  }

}