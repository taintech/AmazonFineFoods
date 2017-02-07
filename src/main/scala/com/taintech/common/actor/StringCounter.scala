package com.taintech.common.actor

import akka.actor.{Actor, ActorLogging}

/**
  * Author: Rinat Tainov 
  * Date: 03/02/2017
  */
class StringCounter extends Actor with ActorLogging {

  import StringCounter._

  val map = scala.collection.mutable.HashMap.empty[String, Int]

  def receive = {
    case s: String =>
      map.get(s) match {
        case Some(i: Int) => map.put(s, i + 1)
        case None => map.put(s, 1)
      }
    case GetTopStrings(i: Int) =>
      sender() ! map.toList.sortBy(_._2).reverse.take(i).map(e => StringCount(e._1, e._2))
  }

}

object StringCounter {

  case class GetTopStrings(i: Int)

  case class StringCount(s: String, n: Int)

}
