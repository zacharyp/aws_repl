package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model._

import scala.collection.JavaConverters._

class ExtendedIAMClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration)
  extends AmazonIdentityManagementClient(awscp, cc) {

  def listUsers(max: Int): List[User] = {
    val request: ListUsersRequest = new ListUsersRequest
    request.setMaxItems(max)
    listUsers(request).getUsers.asScala.toList
  }

  def getUserPolicy(userName: String): GetUserPolicyResult = {
    val request: GetUserPolicyRequest = new GetUserPolicyRequest()
    request.setUserName(userName)
    getUserPolicy(request)
  }

  def listUserPolicies(userName: String): List[String] = {
    val request: ListUserPoliciesRequest = new ListUserPoliciesRequest()
    request.setUserName(userName)

    listUserPolicies(request).getPolicyNames.asScala.toList
  }

  def listPolicies(max: Int): List[Policy] = {
    val request: ListPoliciesRequest = new ListPoliciesRequest
    request.setMaxItems(max)

    listPolicies(request).getPolicies.asScala.toList
  }

  def listRoles(max: Int): List[Role] = {
    val request: ListRolesRequest = new ListRolesRequest
    request.setMaxItems(max)

    listRoles(request).getRoles.asScala.toList
  }
}
