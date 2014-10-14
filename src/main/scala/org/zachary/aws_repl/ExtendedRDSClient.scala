package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.rds.AmazonRDSClient

class ExtendedRDSClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonRDSClient(awscp, cc) {

}
