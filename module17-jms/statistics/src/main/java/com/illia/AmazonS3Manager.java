package com.illia;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AmazonS3Manager {

  private AmazonS3Manager() {
  }

  private static final AmazonS3 s3Client = buildS3Client();

  public static AmazonS3 getS3Client() {
    return s3Client;
  }

  public static AmazonS3 buildS3Client() {

    return AmazonS3ClientBuilder
        .standard()
        .withRegion("eu-north-1")
        .withCredentials(
            new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                    System.getenv("aws_public"),
                    System.getenv("aws_secret"))))
        .build();
  }
}
