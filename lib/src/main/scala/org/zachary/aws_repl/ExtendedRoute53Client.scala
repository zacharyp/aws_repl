package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.route53.AmazonRoute53Client
import com.amazonaws.services.route53.model._

import scala.collection.JavaConverters._

class ExtendedRoute53Client(awscp: AWSCredentialsProvider, cc: ClientConfiguration)
  extends AmazonRoute53Client(awscp, cc) {

  def getHostedZoneId(name: String): String =
    listHostedZones().getHostedZones.asScala.find(_.getName == name).map(_.getId).get

  def addDNSRecord(hostedZoneName: String, dnsName: String, ipAddresses: Seq[String], ttl: Long = 300): Unit = {
    val hostedZoneId = getHostedZoneId(hostedZoneName)
    val records = ipAddresses.map(new ResourceRecord(_))
    val recordSet = new ResourceRecordSet(dnsName, RRType.A).withResourceRecords(records.asJava).withTTL(ttl)
    val changes = List(new Change(ChangeAction.CREATE, recordSet)).asJava

    changeResourceRecordSets(new ChangeResourceRecordSetsRequest(hostedZoneId, new ChangeBatch(changes)))
  }
}
