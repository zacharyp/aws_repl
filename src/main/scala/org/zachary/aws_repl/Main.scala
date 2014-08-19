package org.zachary.aws_repl

import com.amazonaws.services.sqs.AmazonSQSClient

import scala.tools.nsc.interpreter.ILoop
import scala.tools.nsc.Settings
import java.io.{PrintWriter, CharArrayWriter}

object Main extends App {

  def repl = new MainLoop

  val settings = new Settings
  settings.Yreplsync.value = true
  settings.Xnojline.value = true
  settings.deprecation.value = true

  def isRunFromSBT = {
    val c = new CharArrayWriter()
    new Exception().printStackTrace(new PrintWriter(c))
    c.toString.contains("at sbt.")
  }

  if (isRunFromSBT) {
    //an alternative to 'usejavacp' setting, when launching from within SBT
    settings.embeddedDefaults[Main.type]
  } else {
    //use when launching normally outside SBT
    settings.usejavacp.value = true
  }

  repl.process(settings)
  repl.closeInterpreter()
}

class MainLoop extends ILoop {

  val sqs = new AmazonSQSClient

  override def loop(): Unit = {
    intp.bind("e", "Double", 2.71828)
    intp.bind("sqs", "AmazonSQSClient", sqs)
    super.loop()
  }

  addThunk {
    intp.beQuietDuring {
      intp.addImports("com.amazonaws.services.sqs.AmazonSQSClient")
    }
  }
}

