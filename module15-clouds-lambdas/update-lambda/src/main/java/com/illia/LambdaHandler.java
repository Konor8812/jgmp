package com.illia;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LambdaHandler implements RequestStreamHandler {

  @Override
  public void handleRequest(InputStream input, OutputStream output, Context context)
      throws IOException {

    var inputData = input.readAllBytes();
    var objectMapper = new ObjectMapper();
    var model = objectMapper.readValue(inputData, Model.class);

    output.write(("Model deserialized " + model.toString()).getBytes());

    var dynamoDBManager = new AmazonDynamoDBManager();
    var dynamoDBMapper = dynamoDBManager.getDynamoDBMapper();

    try {
      dynamoDBMapper.save(model, new DynamoDBSaveExpression()
          .withExpectedEntry("model_name",
              new ExpectedAttributeValue(new AttributeValue(model.getModel_name()))
                  .withExists(true)));
    } catch (ConditionalCheckFailedException e) {
      output.write("  Couldn't update entity since it does not exist!  ".getBytes());
    } catch (Exception ex) {
      output.write(("  Exception occurred " + ex.getMessage() + "  ").getBytes());
    }
  }
}
