package com.taintech.common.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import FileReader.{EOF, Next}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

/**
  * Author: Rinat Tainov 
  * Date: 03/02/2017
  */
class FileReaderSpec extends TestKit(ActorSystem("test-system"))
  with FlatSpecLike
  with ImplicitSender
  with BeforeAndAfterAll
  with MustMatchers {
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val testfile: String = this.getClass.getClassLoader.getResource("two-lines.txt").getPath

  "Counter Actor" should "handle GetTopString message with using TestProbe" in {
    val sender = TestProbe()
    val reader = system.actorOf(FileReader.props(testfile))

    sender.send(reader, Next)
    val line1 = sender.expectMsgType[String]
    line1 must equal("test line 1")

    sender.send(reader, Next)
    val line2 = sender.expectMsgType[String]
    line2 must equal("test line 2")

    sender.send(reader, Next)
    sender.expectMsg(EOF)
  }
}
