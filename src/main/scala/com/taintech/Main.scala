package com.taintech

object Main {

  def main(args: Array[String]): Unit = {
    akka.Main.main(Array(classOf[FileReaderMaster].getName))
  }

}