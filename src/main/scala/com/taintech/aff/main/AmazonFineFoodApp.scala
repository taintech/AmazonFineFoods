package com.taintech.aff.main

import com.taintech.aff.actor.AmazonFineFoodManager

object AmazonFineFoodApp {

  def main(args: Array[String]): Unit = {
    akka.Main.main(Array(classOf[AmazonFineFoodManager].getName))
  }

}