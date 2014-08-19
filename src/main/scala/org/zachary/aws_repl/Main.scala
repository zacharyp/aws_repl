package org.zachary.aws_repl

import com.amazonaws.services.sqs.AmazonSQSClient

import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.Settings
import java.io.{PrintWriter, CharArrayWriter}


object Main extends App {
  val sqs = new AmazonSQSClient

  def repl = new ILoop {
    override def loop(): Unit = {
      intp.bind("e", "Double", 2.71828)
      //      intp.bind("sqs", "AmazonSQSClient", sqs)
      super.loop()
    }
  }

  val settings = new Settings
  settings.Yreplsync.value = true

  if (isRunFromSBT) {
    //an alternative to 'usejavacp' setting, when launching from within SBT
    settings.embeddedDefaults[Main.type]
  } else {
    //use when launching normally outside SBT
    settings.usejavacp.value = true
  }

  repl.process(settings)

  def isRunFromSBT = {
    val c = new CharArrayWriter()
    new Exception().printStackTrace(new PrintWriter(c))
    c.toString.contains("at sbt.")
  }
}


