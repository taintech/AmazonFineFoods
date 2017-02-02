package com.taintech

import akka.actor.{Actor, ActorLogging}
import com.taintech.Slave.{FailedToParse, NextPlease}

object Slave {
  val HEADER = "Id,ProductId,UserId,ProfileName,HelpfulnessNumerator,HelpfulnessDenominator,Score,Time,Summary,Text"

  /** Sample Data
    * 1,B001E4KFG0,A3SGXH7AUHU8GW,delmartian,1,1,5,1303862400,Good Quality Dog Food,I have bought several of the Vitality canned dog food products and have found them all to be of good quality. The product looks more like a stew than a processed meat and it smells better. My Labrador is finicky and she appreciates this product better than  most.
    * 2,B00813GRG4,A1D87F6ZCVE5NK,dll pa,0,0,1,1346976000,Not as Advertised,"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as ""Jumbo""."
    */
  case class Line(s: String)

  case object NextPlease

  case object FailedToParse

  case object EndOfFile

  case object Done

}

class Slave extends Actor with ActorLogging {

  var count = 0
  var error = 0
  var errors = ""
  val activeUsers = scala.collection.mutable.HashMap.empty[String, Int]
  val commentedFood = scala.collection.mutable.HashMap.empty[String, Int]
  val wordCount = scala.collection.mutable.HashMap.empty[String, Int]

  def receive = {
    case Array(
    id: String,
    productId: String,
    userId: String,
    profileName: String,
    helpfulnessNumerator: String,
    helpfulnessDenominator: String,
    score: String,
    time: String,
    summary: String,
    text: String
    ) =>
      count = count + 1
      //      log.info(text)
      countActiveUser(userId)
      countCommentedItem(productId)
      countWords(text)
      sender() ! NextPlease
    case (FailedToParse, line: String) =>
      //      log.info("failed")
      error = error + 1
      errors = errors + "\n" + line
      sender() ! NextPlease
    case s: String =>
      count = count + 1
      //      log.info(count.toString)
      sender() ! NextPlease
    case Slave.EndOfFile
    =>
      log.info(s"Most active users(UserId): \n${topFrom(activeUsers, 10)}")
      log.info(s"Most commented food(ProductId): \n${topFrom(commentedFood, 10)}")
      log.info(s"Most used word: \n${topFrom(wordCount, 10)}")
      log.info(s"Number Of Lines: $count")
      log.info(s"Number Of Errors: $error")
      log.info(s"Error Lines: $errors")
      sender() ! Slave.Done
  }

  def countHashMap(hashMap: scala.collection.mutable.HashMap[String, Int], value: String): Unit =
    hashMap.get(value) match {
      case Some(i: Int) => hashMap.put(value, i + 1)
      case None => hashMap.put(value, 1)
    }

  def topFrom(hashMap: scala.collection.mutable.HashMap[String, Int], i: Int): String =
    hashMap.toList.sortBy(e => e._2).reverse.take(i).map(e => e._1 + " - " + e._2 + " times").mkString("\n")

  def countActiveUser(userId: String): Unit = countHashMap(activeUsers, userId)

  def countCommentedItem(itemId: String): Unit = countHashMap(commentedFood, itemId)

  def countWords(text: String): Unit = text.toLowerCase.split("\\W+").foreach(countHashMap(wordCount, _))
}