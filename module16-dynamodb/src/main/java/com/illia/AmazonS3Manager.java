package com.illia;

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
    String s3Region = System.getenv("amazon_region");

    return AmazonS3ClientBuilder
        .standard()
        .withRegion(s3Region)
        .build();
  }
}
