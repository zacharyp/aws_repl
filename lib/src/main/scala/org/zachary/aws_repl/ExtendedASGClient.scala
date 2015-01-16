package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.autoscaling.model._

import scala.collection.JavaConverters._

class ExtendedASGClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonAutoScalingClient(awscp, cc) {

  def createTags(asgId: String, tags: Map[String, String], propagateAtLaunch: Boolean = true): Unit = {
    val createRequest: CreateOrUpdateTagsRequest = new CreateOrUpdateTagsRequest
    val tagsList: List[Tag] = tagsMapToTagList(asgId, tags, propagateAtLaunch)
    createRequest.setTags(tagsList.asJava)
    createOrUpdateTags(createRequest)
  }

  def deleteTags(asgId: String, tags: Map[String, String]): Unit = {
    val deleteRequest: DeleteTagsRequest = new DeleteTagsRequest
    val tagsList: List[Tag] = tagsMapToTagList(asgId, tags, false)
    deleteRequest.setTags(tagsList.asJava)
    deleteTags(deleteRequest)
  }

  def attachInstances(asgId: String, instanceIds: List[String]): Unit = {
    val request: AttachInstancesRequest = new AttachInstancesRequest
    request.setAutoScalingGroupName(asgId)
    request.setInstanceIds(instanceIds.asJava)
    attachInstances(request)
  }

  def detachInstances(asgId: String, instanceIds: List[String]): Unit = {
    val request: DetachInstancesRequest = new DetachInstancesRequest
    request.setAutoScalingGroupName(asgId)
    request.setInstanceIds(instanceIds.asJava)
    detachInstances(request)
  }

  def setDesiredCapacity(asgId: String, desiredCapacity: Int, honorCooldown: Boolean = true): Unit = {
    val request: SetDesiredCapacityRequest = new SetDesiredCapacityRequest
    request.setAutoScalingGroupName(asgId)
    request.setDesiredCapacity(desiredCapacity)
    request.setHonorCooldown(honorCooldown)
    setDesiredCapacity(request)
  }

  def setCapacitySizes(asgId: String, minimum: Int, maximum: Int): Unit = {
    val request: UpdateAutoScalingGroupRequest = new UpdateAutoScalingGroupRequest
    request.setAutoScalingGroupName(asgId)
    request.setMinSize(minimum)
    request.setMaxSize(maximum)
    updateAutoScalingGroup(request)
  }

  private def tagsMapToTagList(asgId: String, tags: Map[String, String], propagateAtLaunch: Boolean): List[Tag] = {
    tags.map(t => {
      val tag: Tag = new Tag
      tag.setKey(t._1)
      tag.setValue(t._2)
      tag.setResourceType("auto-scaling-group")
      tag.setResourceId(asgId)
      tag.setPropagateAtLaunch(propagateAtLaunch)
      tag
    }).toList
  }
}
