package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.regions.Region

class Clients(provider: AWSCredentialsProvider, configuration: ClientConfiguration, region: Region) {

  val asg = new ExtendedASGClient(provider, configuration)
  val cloudwatch = new ExtendedCloudWatchClient(provider, configuration)
  val ec2 = new ExtendedEC2Client(provider, configuration)
  val rds = new ExtendedRDSClient(provider, configuration)
  val s3 = new ExtendedS3Client(provider, configuration)
  lazy val sns = new ExtendedSNSClient(provider, configuration, sqs)
  val sqs = new ExtendedSQSClient(provider, configuration)
  val route53 = new ExtendedRoute53Client(provider, configuration)

  val bindings = Map(
    "asg" -> asg,
    "cloudwatch" -> cloudwatch,
    "ec2" -> ec2,
    "rds" -> rds,
    "route53" -> route53,
    "s3" -> s3,
    "sns" -> sns,
    "sqs" -> sqs
  )

  bindings.foreach { case (name, instance) =>
    instance.setRegion(region)
                   }

}
