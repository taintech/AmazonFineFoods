package com.taintech.aff.main

import com.taintech.aff.actor.FileReaderMaster

object AmazonFineFoodApp {

  def main(args: Array[String]): Unit = {
    akka.Main.main(Array(classOf[FileReaderMaster].getName))
  }

}