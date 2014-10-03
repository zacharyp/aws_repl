package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DeregisterImageRequest

class ExtendedEC2Client(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonEC2Client(awscp, cc) {

  def deregisterImage(imageId: String): Unit = {
    deregisterImage(new DeregisterImageRequest(imageId))
  }
}
