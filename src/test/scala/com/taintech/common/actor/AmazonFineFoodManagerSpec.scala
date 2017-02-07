package com.taintech.common.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.taintech.aff.actor.AmazonFineFoodManager.{Review, ReviewParser}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

/**
  * Author: Rinat Tainov 
  * Date: 07/02/2017
  */
class AmazonFineFoodManagerSpec  extends TestKit(ActorSystem("test-system"))
  with FlatSpecLike
  with ImplicitSender
  with BeforeAndAfterAll
  with MustMatchers {

  import AmazonFineFoodManagerSpec._

  "ReviewParser" should "parse test line with values test" in {
    val rp = new ReviewParser()
    rp.parse(testLine) must equal(Review("test3", "test2", "test10"))
  }

  
}

object AmazonFineFoodManagerSpec {
  val testLine = "test1,test2,test3,test4,test5,test6,test7,test8,test9,test10"
  val mockReview: String => Review = _ => Review("test user", "test product", "test text")
}

