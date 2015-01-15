package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.regions.Region

class Clients(provider: AWSCredentialsProvider, configuration: ClientConfiguration, region: Region) {

  val s3 = new ExtendedS3Client(provider, configuration)
  val sqs = new ExtendedSQSClient(provider, configuration)
  val sns = new ExtendedSNSClient(provider, configuration, sqs)
  val ec2 = new ExtendedEC2Client(provider, configuration)
  val rds = new ExtendedRDSClient(provider, configuration)
  val cloudwatch = new ExtendedCloudWatchClient(provider, configuration)

  val bindings = Map(
    "s3" -> s3,
    "sqs" -> sqs,
    "sns" -> sns,
    "ec2" -> ec2,
    "rds" -> rds,
    "cloudwatch" -> cloudwatch
  )

  bindings.foreach { case (name, instance) =>
    instance.setRegion(region)
  }

}
