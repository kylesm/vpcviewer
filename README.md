Overview
==========================

[![Build Status](https://travis-ci.org/kylesm/vpcviewer.svg?branch=master)](https://travis-ci.org/kylesm/vpcviewer)

vpcviewer provides a single-screen view of routing tables and subnet information for VPCs in AWS. When standing up or troubleshooting VPCs I found myself constantly flipping back and forth between the route table and subnet views in the AWS console. I wanted a single screen that would show all of the subnets in a given VPC as well as their route table and network information.

The UI is read-only and individual items link directly to the resource in the AWS console where appropriate. Minimal AWS permissions are required for the app to work.


Building
--------

vpcviewer requires JDK 8 to build. You can bring your own Gradle distribution or just use the included Gradle wrapper (`gradlew`).

This is the initial sequence of steps to build the project:

```
npm install
bower install
grunt wiredep
gradle bootRepackage
```


Running
-------

If you're running in your local development environment you can just `gradle bootRun` and then access [localhost:8080](http://localhost:8080).

In other environments simply deploy the WAR file to your container of choice or, you can copy the Boot-repackaged WAR to a machine and run: `java -jar vpcviewer-<version>.war`

vpcviewer uses the [DefaultAWSCredentialsProviderChain](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/DefaultAWSCredentialsProviderChain.html) and will look at several different places for AWS credentials:

1. Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY or AWS_ACCESS_KEY and AWS_SECRET_KEY
2. Java System Properties - aws.accessKeyId and aws.secretKey
3. Credential profiles file at the default location (~/.aws/credentials)
4. Instance profile credentials delivered through the Amazon EC2 metadata service

See the linked Javadoc for DefaultAWSCredentialsProviderChain for additional details.


Required AWS permissions
------------------------

The following permissions are required for vpcviewer to function:
- describe route tables
- describe subnets

