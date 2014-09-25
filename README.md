aws_repl
========

Scala AWS REPL

sbt run

Takes AWS credentials from the following, in order:

1 : .aws/config based off the passed profile

sbt "run --proxyPort 8080 --proxyHost somehost.com --profile prod --region us-west-2"

2 : AWS_ACCESS_KEY and AWS_SECRET_KEY ENV variables

sbt "run --proxyPort 8080 --proxyHost somehost.com --region us-west-2"

3 : passed system properties aws.accessKeyId and aws.secretKey, i.e.

sbt -Daws.accessKeyId=foo -Daws.secretKey=bar "run --region us-west-2 --proxyHost somehost.com --proxyPort 8080"