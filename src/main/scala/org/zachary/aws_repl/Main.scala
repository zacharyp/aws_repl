package org.zachary.aws_repl

import java.io.{CharArrayWriter, PrintWriter}

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth._
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sns.AmazonSNSClient

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.ILoop

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
  val parser = new scopt.OptionParser[Config]("scopt") {
    head("scopt", "3.x")
    opt[Int]("proxyPort") action { (x, c) => c.copy(proxyPort = Option(x))} optional()
    opt[String]("proxyHost") action { (x, c) => c.copy(proxyHost = Option(x))} optional()
    opt[String]("profile") action { (x, c) => c.copy(profile = Option(x))} optional()
    opt[String]("region") action { (x, c) => c.copy(region = Option(x))} optional()
  }

  private val configuration: ClientConfiguration = new ClientConfiguration

  parser.parse(args, Config()) map { config => {
    config.proxyHost.foreach(configuration.setProxyHost)
    config.proxyPort.foreach(configuration.setProxyPort)
  }}

  private val provider: ProfileCredentialsProvider = parser.parse(args, Config()).flatMap {
      _.profile.map(new ProfileCredentialsProvider(_))}.getOrElse(new ProfileCredentialsProvider)

  private val region: Region = parser.parse(args, Config()).map { config =>
    config.region.getOrElse("us-west-2") }.map(r => Region.getRegion(Regions.fromName(r))).get

  private val chain: AWSCredentialsProviderChain = new AWSCredentialsProviderChain(provider,
    new EnvironmentVariableCredentialsProvider,
    new SystemPropertiesCredentialsProvider)

  val s3 = new AmazonS3Client(chain, configuration)
  s3.setRegion(region)
  val sqs = new AmazonSQSClient(chain, configuration)
  sqs.setRegion(region)
  val sns = new AmazonSNSClient(chain, configuration)
  sns.setRegion(region)
  val ec2 = new ExtendedEC2Client(chain, configuration)
  ec2.setRegion(region)

  override def loop(): Unit = {
    intp.bind("s3", s3.getClass.getCanonicalName, s3)
    intp.bind("sqs", sqs.getClass.getCanonicalName, sqs)
    intp.bind("sns", sns.getClass.getCanonicalName, sns);
    intp.bind("ec2", ec2.getClass.getCanonicalName, ec2);
    super.loop()
  }

    addThunk {
      intp.beQuietDuring {
        intp.addImports("com.amazonaws.services.s3.AmazonS3Client")
        intp.addImports("com.amazonaws.services.sqs.AmazonSQSClient")
        intp.addImports("com.amazonaws.services.sns.AmazonSNSClient")
        intp.addImports("com.amazonaws.services.ec2.AmazonEC2Client")
      }
    }
}

case class Config(
  profile: Option[String] = None,
  region: Option[String] = None,
  proxyHost: Option[String] = None,
  proxyPort: Option[Int] = None)
