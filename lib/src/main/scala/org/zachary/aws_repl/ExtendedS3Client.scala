package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectListing

import scala.collection.JavaConverters._

class ExtendedS3Client(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonS3Client(awscp, cc) {

//  def emptyAndRemoveBucket(bucketName: String): Unit = {
//    val objects: ObjectListing = listObjects(bucketName)
//    objects.getObjectSummaries.asScala.map(_.getKey).foreach(deleteObject(bucketName, _))
//    deleteBucket(bucketName)
//  }

}
