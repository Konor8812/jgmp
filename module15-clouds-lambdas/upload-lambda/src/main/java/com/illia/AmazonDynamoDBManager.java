package com.illia;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class AmazonDynamoDBManager {

  public DynamoDBMapper getDynamoDBMapper() {
    String accessKey = System.getenv("amazon_aws_accesskey");
    String secretKey = System.getenv("amazon_aws_secretkey");
    String dynamoDBRegion = System.getenv("amazon_dynamodb_region");

    var awsCredentialsProvider = awsCredentialsProvider(accessKey, secretKey);
    var amazonDynamoDBClient = amazonDynamoDB(dynamoDBRegion, awsCredentialsProvider);

    return new DynamoDBMapper(amazonDynamoDBClient);
  }

  public AmazonDynamoDB amazonDynamoDB(String amazonDynamoDBRegion,
      AWSCredentialsProvider awsCredentialsProvider) {
    var amazonDynamoDBEndpoint = "https://dynamodb." + amazonDynamoDBRegion + ".amazonaws.com";

    return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonDynamoDBRegion))
        .withCredentials(awsCredentialsProvider).build();
  }

  public AWSCredentialsProvider awsCredentialsProvider(String amazonAWSAccessKey,
      String amazonAWSSecretKey) {
    return new AWSStaticCredentialsProvider(
        new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey));
  }

}
