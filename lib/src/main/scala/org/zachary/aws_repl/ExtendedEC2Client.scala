package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.{CreateImageResult, CreateImageRequest, DeregisterImageRequest}

class ExtendedEC2Client(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonEC2Client(awscp, cc) {

  def createImage(instanceId: String, imageName: String, description: String, noReboot: Boolean = true): String = {
    val request: CreateImageRequest = new CreateImageRequest
    request.setInstanceId(instanceId)
    request.setName(imageName)
    request.setDescription(description)
    request.setNoReboot(noReboot)
    val image: CreateImageResult = createImage(request)

    image.getImageId
  }

  def deregisterImage(amiId: String): Unit = {
    deregisterImage(new DeregisterImageRequest(amiId))
  }
}
