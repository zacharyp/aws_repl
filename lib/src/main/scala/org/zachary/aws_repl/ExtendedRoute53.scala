package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.route53domains.AmazonRoute53DomainsClient

class ExtendedRoute53(awscp: AWSCredentialsProvider, cc: ClientConfiguration)
  extends AmazonRoute53DomainsClient(awscp, cc) {

}
