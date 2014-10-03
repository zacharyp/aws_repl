package org.zachary.aws_repl

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.{QueueDoesNotExistException, SetQueueAttributesRequest}
import com.amazonaws.{AmazonServiceException, ClientConfiguration}
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}

import scala.collection.JavaConverters._

case class RedrivePolicy(
  deadLetterTargetArn: String,
  maxReceiveCount: Int
)

class ExtendedSQSClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonSQSClient(awscp, cc) {

  def setQueueMaximumMessageSize(queueName: String, size: Int = 262144): Unit = {
    try {
      val attributes = Map[String, String]("MaximumMessageSize" -> size.toString)
      val request = new SetQueueAttributesRequest(getQueueUrl(queueName).getQueueUrl, attributes.asJava)
      setQueueAttributes(request)
    } catch {
      case ex: QueueDoesNotExistException => println("The specified queue does not exist.")
      case ex: AmazonServiceException => println("Invalid queue size.")
    }
  }

  def setQueueMaximumRetentionPeriod(queueName: String, retentionPeriodSeconds: Int = 345600): Unit = {
    try {
      val attributes = Map[String, String]("MessageRetentionPeriod" -> retentionPeriodSeconds.toString)
      val request = new SetQueueAttributesRequest(getQueueUrl(queueName).getQueueUrl, attributes.asJava)
      setQueueAttributes(request)
    } catch {
      case ex: QueueDoesNotExistException => println("The specified queue does not exist.")
      case ex: AmazonServiceException => println("Invalid retention period.")
    }
  }

  def setQueueVisibilityTimeout(queueName: String, visibilityTimeoutSeconds: Int = 30): Unit = {
    try {
      val attributes = Map[String, String]("VisibilityTimeout" -> visibilityTimeoutSeconds.toString)
      val request = new SetQueueAttributesRequest(getQueueUrl(queueName).getQueueUrl, attributes.asJava)
      setQueueAttributes(request)
    } catch {
      case ex: QueueDoesNotExistException => println("The specified queue does not exist.")
      case ex: AmazonServiceException => println("Invalid visibility timeout.")
    }
  }

  def setQueueRedrivePolicy(queueName: String, deadLetterTargetArn: String, maxReceiveCount: Int = 5): Unit = {
    implicit val formats = Serialization.formats(NoTypeHints)
    try {
      val redrivePolicy: String = write(RedrivePolicy(deadLetterTargetArn, maxReceiveCount))
      val attributes = Map[String, String]("RedrivePolicy" -> redrivePolicy)
      val request = new SetQueueAttributesRequest(getQueueUrl(queueName).getQueueUrl, attributes.asJava)
      setQueueAttributes(request)
    } catch {
      case ex: QueueDoesNotExistException => println("The specified queue does not exist.")
      case ex: AmazonServiceException => println("Invalid visibility timeout.")
    }
  }
}
