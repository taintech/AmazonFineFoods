package com.taintech

import akka.actor.{Actor, ActorLogging}

object Slave {
  val HEADER = "Id,ProductId,UserId,ProfileName,HelpfulnessNumerator,HelpfulnessDenominator,Score,Time,Summary,Text"

  /** Sample Data
    * 1,B001E4KFG0,A3SGXH7AUHU8GW,delmartian,1,1,5,1303862400,Good Quality Dog Food,I have bought several of the Vitality canned dog food products and have found them all to be of good quality. The product looks more like a stew than a processed meat and it smells better. My Labrador is finicky and she appreciates this product better than  most.
    * 2,B00813GRG4,A1D87F6ZCVE5NK,dll pa,0,0,1,1346976000,Not as Advertised,"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as ""Jumbo""."
    */
  case class Line(s: String)
  case object EndOfFile
  case object Done
}

class Slave extends Actor with ActorLogging {

  var count = 0

  def receive = {
    case Slave.Line(s) =>
      count = count + 1
    case Slave.EndOfFile =>
      log.info(s"Number Of Lines: $count")
      sender() ! Slave.Done
  }
}