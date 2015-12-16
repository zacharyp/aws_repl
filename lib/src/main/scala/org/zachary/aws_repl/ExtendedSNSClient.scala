package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.sns.AmazonSNSClient

import scala.collection.JavaConverters._

class ExtendedSNSClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration, sqs: ExtendedSQSClient)
  extends AmazonSNSClient(awscp, cc) {

  def subscribeQueue(topicArn: String, queueName: String, raw: Boolean = false): Unit = {
    subscribeQueueByQueueARN(topicArn, sqs.getQueueArn(queueName), raw)
  }

  def subscribeQueueByQueueARN(topicArn: String, queueArn: String, raw: Boolean = false): Unit = {
    val subscribeResult = subscribe(topicArn, "sqs", queueArn)

    if (raw) {
      val arn: String = subscribeResult.getSubscriptionArn
      setSubscriptionAttributes(arn, "RawMessageDelivery", "true")
    }
  }

  def unsubscribeQueue(topicArn: String, queueName: String): Unit = {
    val queueArn = sqs.getQueueArn(queueName)
    unsubscribeQueueByArn(topicArn, queueArn)
  }

  def unsubscribeQueueByArn(topicArn: String, queueArn: String): Unit = {
    listSubscriptionsByTopic(topicArn).getSubscriptions.asScala.filter(_.getEndpoint == queueArn).foreach(sub => {
      unsubscribe(sub.getSubscriptionArn)
    })

  }
}
