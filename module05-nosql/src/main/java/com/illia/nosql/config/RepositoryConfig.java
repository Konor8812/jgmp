package com.illia.nosql.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories(basePackages = { "com.illia.nosql.repository" })
public class RepositoryConfig extends AbstractCouchbaseConfiguration {

  @Value("${couchbase.connectionString}")
  private String connectionString;
  @Value("${couchbase.bucket-name}")
  private String bucketName;
  @Value("${couchbase.bucket-user-name}")
  public String bucketUsername;
  @Value("${couchbase.bucket-user-pass}")
  public String bucketPassword;

  @Override
  public String getConnectionString() {
    return connectionString;
  }

  @Override
  public String getUserName() {
    return bucketUsername;
  }

  @Override
  public String getPassword() {
    return bucketPassword;
  }

  @Override
  public String getBucketName() {
    return bucketName;
  }

}
