package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.sns.AmazonSNSClient

import scala.collection.JavaConverters._

class ExtendedSNSClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration, sqs: ExtendedSQSClient)
  extends AmazonSNSClient(awscp, cc) {

  def subscribeQueue(topicArn: String, queueName: String): Unit = {
    subscribe(topicArn, "sqs", sqs.getQueueArn(queueName))
  }

  def unsubscribeQueue(topicArn: String, queueName: String): Unit = {
    val queueArn = sqs.getQueueArn(queueName)
    listSubscriptionsByTopic(topicArn).getSubscriptions.asScala.filter(_.getEndpoint == queueArn).foreach(sub => {
      unsubscribe(sub.getSubscriptionArn)
    })
  }
}
