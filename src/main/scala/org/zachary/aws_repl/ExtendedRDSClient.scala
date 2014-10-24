package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.rds.AmazonRDSClient
import com.amazonaws.services.rds.model.{DescribeDBInstancesRequest, DBInstance, DeleteDBInstanceRequest}

import scala.util.Try

class ExtendedRDSClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration) extends AmazonRDSClient(awscp, cc) {

  def describeDBInstancesRequest(dBInstanceIdentifier: String) =
    new DescribeDBInstancesRequest().withDBInstanceIdentifier(dBInstanceIdentifier)

  def deleteDBInstanceByName(dbInstanceIdentifier: String,
                             skipFinalSnapshot: Boolean = false,
                             finalDBSnapshoIdentifier: Option[String] = None) = {
    val baseRequest = (new DeleteDBInstanceRequest).withDBInstanceIdentifier(dbInstanceIdentifier)
                                                   .withSkipFinalSnapshot(skipFinalSnapshot)
    val request = finalDBSnapshoIdentifier.flatMap(id => Some(baseRequest.withFinalDBSnapshotIdentifier(id)))
                                          .getOrElse(baseRequest)
    this.deleteDBInstance(request)
  }

  def describeDBInstance(dbInstanceIdentifier: String): Option[DBInstance] = {
    val instancesResult = Try(this.describeDBInstances(describeDBInstancesRequest(dbInstanceIdentifier)).getDBInstances.iterator.next)
    Option(instancesResult.getOrElse(null))
  }

}

