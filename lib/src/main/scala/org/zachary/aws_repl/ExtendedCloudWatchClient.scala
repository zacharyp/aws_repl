package org.zachary.aws_repl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient
import com.amazonaws.services.cloudwatch.model.{DescribeAlarmsRequest, MetricAlarm, PutMetricAlarmRequest}

import scala.collection.JavaConverters._
import scala.collection.mutable

class ExtendedCloudWatchClient(awscp: AWSCredentialsProvider, cc: ClientConfiguration)
  extends AmazonCloudWatchClient(awscp, cc) {

  def describeAlarm(alarmName: String): Unit = {
    val dar: DescribeAlarmsRequest = new DescribeAlarmsRequest
    dar.setAlarmNames(List(alarmName).asJava)

    val alarm: MetricAlarm = describeAlarms(dar).getMetricAlarms.asScala.toList(0)
    print(alarm)
  }

  private def updateAlarm(alarmName: String, f: PutMetricAlarmRequest => Unit): Unit = {
    val dar: DescribeAlarmsRequest = new DescribeAlarmsRequest
    dar.setAlarmNames(List(alarmName).asJava)

    val alarmOption: Option[MetricAlarm] = describeAlarms(dar).getMetricAlarms.asScala.toList.find(p => {
      p.getAlarmName.toUpperCase == alarmName.toUpperCase
    })

    alarmOption.map(alarm => {
      val request: PutMetricAlarmRequest = new PutMetricAlarmRequest
      request.setActionsEnabled(alarm.getActionsEnabled)
      request.setAlarmActions(alarm.getAlarmActions)
      request.setAlarmDescription(alarm.getAlarmDescription)
      request.setAlarmName(alarm.getAlarmName)
      request.setComparisonOperator(alarm.getComparisonOperator)
      request.setDimensions(alarm.getDimensions)
      request.setEvaluationPeriods(alarm.getEvaluationPeriods)
      request.setMetricName(alarm.getMetricName)
      request.setNamespace(alarm.getNamespace)
      request.setOKActions(alarm.getOKActions)
      request.setPeriod(alarm.getPeriod)
      request.setStatistic(alarm.getStatistic)
      request.setThreshold(alarm.getThreshold)
      f(request)
    }).getOrElse(print(s"Alarm '$alarmName' not found"))
  }

  def updateAlarmThreshold(alarmName: String, threshHold: Double): Unit = {
    updateAlarm(alarmName, req => {
      req.setThreshold(threshHold)
      putMetricAlarm(req)
      print(s"Threshold $threshHold set for $alarmName")
    })
  }

  def updateAlarmPeriod(alarmName: String, periodSeconds: Int, evaluationPeriods: Int = 1): Unit = {
    val periodsAllowed: List[Int] = List(60, 300, 900, 3600, 21600)
    if (!periodsAllowed.contains(periodSeconds)) {
      print(s"Period Seconds must be one of $periodsAllowed")
      return
    }

    updateAlarm(alarmName, req => {
      req.setPeriod(periodSeconds)
      req.setEvaluationPeriods(evaluationPeriods)
      putMetricAlarm(req)
      print(s"Alarm notifies on: $evaluationPeriods consecutive period(s) of $periodSeconds seconds")
    })
  }

  def changeOKAction(alarmName: String, okActionArn: String): Unit = {
    updateAlarm(alarmName, req => {
      req.setOKActions(List(okActionArn).asJava)
      putMetricAlarm(req)
      print(s"OK Action '$okActionArn' set for alarm $alarmName")
    })
  }

  def changeAlarmAction(alarmName: String, alarmArn: String): Unit = {
    updateAlarm(alarmName, req => {
      req.setAlarmActions(List(alarmArn).asJava)
      putMetricAlarm(req)
      print(s"Alarm Action '$alarmArn' set for alarm $alarmName")
    })
  }
}
