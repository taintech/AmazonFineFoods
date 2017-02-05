package com.taintech.aff.actor

import java.security.MessageDigest

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask

import scala.concurrent.duration._
import akka.util.Timeout
import au.com.bytecode.opencsv.CSVParser
import com.taintech.common.actor.FileReader.{EOF, Line, Next}
import com.taintech.common.actor.StringCounter.{GetTopStrings, StringCount}
import com.taintech.common.actor.{FileReader, StringCounter}

/**
  * Author: Rinat Tainov
  * Date: 04/02/2017
  */
class AmazonFineFoodManager extends Actor with ActorLogging {

  val uniques = scala.collection.mutable.HashSet.empty[Array[Byte]]

  var userStats: ActorRef = _
  var productStats: ActorRef = _
  var wordStats: ActorRef = _

  import AmazonFineFoodManager._

  override def preStart(): Unit = {

    log.info("Starting...")

    userStats = context.actorOf(Props[StringCounter], "userStats")
    productStats = context.actorOf(Props[StringCounter], "productStats")
    wordStats = context.actorOf(Props[StringCounter], "wordStats")

    val reader = context.actorOf(FileReader.props(filePath))

    reader ! FileReader.IgnoreHeader
    reader ! FileReader.Next
  }

  def receive = {
    case Line(s: String) =>
      val hash = md5(s)
      if (!uniques.contains(hash)){
        uniques.add(hash)
        val review = parse(s)
        review.text.toLowerCase.split("\\W+").foreach(e => wordStats ! e)
        userStats ! review.userId
        productStats ! review.productId
      }
      sender() ! Next
    case EOF =>
      import scala.concurrent.ExecutionContext.Implicits.global
      implicit val timeout = Timeout(5 seconds)
      val f = for {
        mostActiveUsers <- (userStats ? GetTopStrings(10)).mapTo[List[StringCount]]
        mostCommentedItems <- (productStats ? GetTopStrings(10)).mapTo[List[StringCount]]
        mostUsedWords <- (wordStats ? GetTopStrings(10)).mapTo[List[StringCount]]
      } yield {
        log.info(mostActiveUsers.toString())
        log.info(mostCommentedItems.toString())
        log.info(mostUsedWords.toString())
      }
      f.onComplete {
        x => context.stop(self)
      }
  }
}

object AmazonFineFoodManager {

  //TODO as argument
  val filePath = "/Users/taintech/Downloads/amazon-fine-foods/Reviews.csv"

  case class Review(userId: String, productId: String, text: String)

  val csv = new CSVParser(',', '"', 0, false)

  def parse(line: String): Review = csv.parseLine(line) match {
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
    ) => Review(userId, productId, text)
  }

  def md5(s: String): Array[Byte] = {
    MessageDigest.getInstance("MD5").digest(s.getBytes).take(42)
  }

}