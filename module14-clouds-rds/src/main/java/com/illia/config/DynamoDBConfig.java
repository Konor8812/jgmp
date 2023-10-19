package com.illia.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {


  @Bean
  public DynamoDBMapper mapper(AmazonDynamoDB client) {
    return new DynamoDBMapper(client);
  }


  @Bean
  public AmazonDynamoDB amazonDynamoDB(
      @Value("${amazon.dynamodb.region}") String amazonDynamoDBRegion,
      AWSCredentialsProvider awsCredentialsProvider) {
    var amazonDynamoDBEndpoint = "https://dynamodb." + amazonDynamoDBRegion + ".amazonaws.com";

    return AmazonDynamoDBClientBuilder.standard()
        .withEndpointConfiguration(new AwsClientBuilder
            .EndpointConfiguration(amazonDynamoDBEndpoint, amazonDynamoDBRegion))
        .withCredentials(awsCredentialsProvider)
        .build();
  }

  @Bean
  public AWSCredentialsProvider awsCredentialsProvider(
      @Value("${amazon.aws.accesskey}") String amazonAWSAccessKey,
      @Value("${amazon.aws.secretkey}") String amazonAWSSecretKey) {
    return new AWSStaticCredentialsProvider(
        new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey));
  }
}
