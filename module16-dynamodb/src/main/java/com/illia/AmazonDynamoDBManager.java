package com.illia;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class AmazonDynamoDBManager {

  private AmazonDynamoDBManager() {
  }

  private static final DynamoDBMapper dynamoDBMapper = buildDynamoDnMapper();

  public static DynamoDBMapper getDynamoDBMapper() {
    return dynamoDBMapper;
  }

  public static DynamoDBMapper buildDynamoDnMapper() {
    String dynamoDBRegion = System.getenv("amazon_region");
    var amazonDynamoDBEndpoint = "https://dynamodb." + dynamoDBRegion + ".amazonaws.com";

    return new DynamoDBMapper(
        AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(new AwsClientBuilder
                .EndpointConfiguration(amazonDynamoDBEndpoint, dynamoDBRegion))
            .build());
  }
}
