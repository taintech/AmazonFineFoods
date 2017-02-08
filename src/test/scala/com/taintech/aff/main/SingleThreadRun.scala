package com.taintech.aff.main

import scala.io.Source

/**
  * Author: Rinat Tainov 
  * Date: 08/02/2017
  */
object SingleThreadRun {
  def main(args: Array[String]) = {
    val lines = Source.fromFile("/Users/taintech/Downloads/amazon-fine-foods/Reviews.csv").getLines()
    var count = 0
    for {
      line <- lines
      word <- line.toLowerCase.split("\\W+")
      if word == "the"
    } count = count + 1
    println(count)
  }
}
