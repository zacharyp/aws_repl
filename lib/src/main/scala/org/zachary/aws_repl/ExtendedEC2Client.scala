package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model._

import scala.collection.JavaConverters._

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

  def createTags(instanceId: String, tags: Map[String, String]): Unit = {
    val request: CreateTagsRequest = new CreateTagsRequest
    request.setResources(List(instanceId).asJava)
    request.setTags(tags.map(t => {
      new Tag(t._1, t._2)
    }).toList.asJava)
    createTags(request)
  }

  def deleteTags(instanceId: String, tags: Map[String, String]): Unit = {
    val request: DeleteTagsRequest = new DeleteTagsRequest
    request.setResources(List(instanceId).asJava)
    request.setTags(tags.map(t => {
      new Tag(t._1, t._2)
    }).toList.asJava)
    deleteTags(request)
  }

  // ec2.createTagsOnInstancesByName("foo-node-*", "projectid", "some.value")
  def createTagsOnInstancesByName(instanceName: String, tagName: String, tagValue: String): Unit = {
    val filters: Filter = new Filter("tag:Name", List(s"$instanceName").asJava)
    val request: DescribeInstancesRequest = new DescribeInstancesRequest
    request.setFilters(List(filters).asJava)

    val instances: DescribeInstancesResult = describeInstances(request)
    instances.getReservations.iterator().asScala.foreach(reservation => {
      reservation.getInstances.asScala.foreach(instance => {
        createTags(instance.getInstanceId, Map(tagName -> tagValue))
        println(s"Setting tag $tagName for ${instance.getInstanceId}")
      })
    })
  }

  def deregisterImage(amiId: String): Unit = {
    deregisterImage(new DeregisterImageRequest(amiId))
  }

  case class InstancesResult(instanceValues: List[InstanceValues]) {

    def print: String = {
      instanceValues.map(_.toString).mkString("\n")
    }

    def printIPs: String = {
      instanceValues.map(_.toIPs).mkString("\n")
    }

  }

  case class InstanceValues(
    instanceId: String,
    state: String,
    privateIPAddress: String,
    publicIPAddress: String
  ) {

    override def toString: String = {
      f"$instanceId%-12s$state%-10s$privateIPAddress%-15s$publicIPAddress"
    }

    def toIPs: String = {
      f"$privateIPAddress%-15s$publicIPAddress"
    }
  }

  def findInstanceValuesByTagName(tagName: String): InstancesResult = {
    val filters: Filter = new Filter("tag:Name", List(tagName).asJava)
    val request = new DescribeInstancesRequest
    request.setFilters(List(filters).asJava)

    InstancesResult(describeInstances(request).getReservations.iterator().asScala.flatMap(reservation => {
      reservation.getInstances.asScala.map(i => {
        InstanceValues(i.getInstanceId, i.getState.getName, i.getPrivateIpAddress, i.getPublicIpAddress)
      })
    }).toList.sortBy(_.privateIPAddress))
  }
}
