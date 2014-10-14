package org.zachary.aws_repl

import java.io.{CharArrayWriter, PrintWriter}
import java.net.URL

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth._
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.{Region, Regions}

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.{NamedParam, ILoop}

object Main extends App {

  def repl = new MainLoop(args)

  val settings = new Settings
  settings.Yreplsync.value = true
//  settings.Xnojline.value = true  // Turns off tab completion
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
}

class MainLoop(args: Array[String]) extends ILoop {

  val parser = new scopt.OptionParser[Config]("aws_repl") {
    head("aws_repl", "1.0")
    opt[Int]("proxyPort") action { (x, c) => c.copy(proxyPort = Option(x))} optional()
    opt[String]("proxyHost") action { (x, c) => c.copy(proxyHost = Option(x))} optional()
    opt[String]("profile") action { (x, c) => c.copy(profile = Option(x))} optional()
    opt[String]("region") action { (x, c) => c.copy(region = Option(x))} optional()
  }

  val (configuration: ClientConfiguration, provider: AWSCredentialsProvider, region: Region) =
    parser.parse(args, Config()).map({ config: Config =>

      val envProxy: Option[String] = sys.env.get("HTTP_PROXY") orElse
                                     sys.env.get("http_proxy") orElse
                                     sys.env.get("HTTPS_PROXY") orElse
                                     sys.env.get("https_proxy")
      val proxyHostFromEnv: Option[String] = envProxy map(new URL(_).getHost)
      val proxyPortFromEnv: Option[Int] = envProxy map(new URL(_).getPort)

      config.proxyHost orElse proxyHostFromEnv getOrElse(null)

      val configuration = new ClientConfiguration()
        .withProxyHost(config.proxyHost orElse proxyHostFromEnv getOrElse(null))
        .withProxyPort(config.proxyPort orElse proxyPortFromEnv getOrElse(-1))
      val provider = config.profile map(new ProfileCredentialsProvider(_)) getOrElse new DefaultAWSCredentialsProviderChain
      val region = Region.getRegion(Regions.fromName(config.region.getOrElse("us-west-2")))

      (configuration, provider, region)
    }).getOrElse(throw new RuntimeException("Could not config options."))

  val clients = new Clients(provider, configuration, region)

  override def loop(): Unit = {
    clients.bindings.foreach { case (name, instance) =>
        intp.quietBind(NamedParam(name, instance.getClass.getCanonicalName, instance))
    }
    super.loop()
  }

  addThunk {
    intp.beQuietDuring {
      intp.addImports("com.amazonaws.services.s3._")
    }
  }
}

case class Config(
  profile: Option[String] = None,
  region: Option[String] = None,
  proxyHost: Option[String] = None,
  proxyPort: Option[Int] = None)
