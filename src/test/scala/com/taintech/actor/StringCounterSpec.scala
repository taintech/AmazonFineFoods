package com.taintech.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.taintech.actor.StringCounter.{GetTopStrings, StringCount}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

/**
  * Author: Rinat Tainov 
  * Date: 03/02/2017
  */
class StringCounterSpec extends TestKit(ActorSystem("test-system"))
  with FlatSpecLike
  with ImplicitSender
  with BeforeAndAfterAll
  with MustMatchers {
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Counter Actor" should "handle GetTopString message with using TestProbe" in {
    val sender = TestProbe()

    val counter = system.actorOf(Props[StringCounter])

    sender.send(counter, "a")
    sender.send(counter, "c")
    sender.send(counter, "c")
    sender.send(counter, "b")
    sender.send(counter, "b")
    sender.send(counter, "c")

    sender.send(counter, GetTopStrings(2))
    val state = sender.expectMsgType[List[StringCount]]
    state must equal(List(StringCount("c", 3), StringCount("b", 2)))
  }
}
