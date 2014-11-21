package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client

class ExtendedS3Client(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonS3Client(awscp, cc) {

}
