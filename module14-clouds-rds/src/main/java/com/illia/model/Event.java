package com.illia.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@DynamoDBTable(tableName = "_events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {

  @DynamoDBHashKey(attributeName = "id")
  private long id;
  @DynamoDBAttribute(attributeName = "title")
  private String title;
  @DynamoDBAttribute(attributeName = "date")
  private String dateAsString;
}
