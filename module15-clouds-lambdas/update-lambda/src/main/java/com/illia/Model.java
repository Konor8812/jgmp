package com.illia;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "jgmp")
public class Model {

  @DynamoDBHashKey
  private String model_name;
  private long price;
  private String picture_url;
}
