package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.sns.AmazonSNSClient

class ExtendedSNSClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonSNSClient(awscp, cc) {

}
