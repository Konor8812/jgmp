package com.illia;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, Void> {

  @Override
  public Void handleRequest(Map<String, Object> input, Context context) {
    var inputAsList = (List) input.get("Records");

    List<Model> lastChanged = new ArrayList<>();
    for (var record : inputAsList) {
      var content = (LinkedHashMap) record;
      var dynamodb = (LinkedHashMap) content.get("dynamodb");
      var newImage = (LinkedHashMap<?,
          LinkedHashMap<?,
              LinkedHashMap<?, ?>>>) dynamodb.get("NewImage");

      var addedModel = new Model();
      addedModel.setModel_name(
          String.valueOf(
              newImage
                  .get("model_name")
                  .get("S")));
      addedModel.setPrice(Long.parseLong(
          String.valueOf(
              newImage
                  .get("price")
                  .get("N"))));
      addedModel.setPicture_url(
          String.valueOf(
              newImage
                  .get("picture_url")
                  .get("S")));
      lastChanged.add(addedModel);
    }

    var s3client = AmazonS3Manager.getS3Client();
    var dynamoDBMapper = AmazonDynamoDBManager.getDynamoDBMapper();

    var result = dynamoDBMapper.scan(Model.class, new DynamoDBScanExpression());

    var htmlBuilder = new StringBuilder();
    htmlBuilder.append(
        "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"><title>Models</title></head><body><h1>Available models:</h1><ol>");
    for (var model : result) {
      htmlBuilder.append("<li>")
          .append(model);
      if (lastChanged.contains(model)) {
        htmlBuilder.append(" || last added");
      }
      htmlBuilder
          .append("</li>");
    }
    htmlBuilder.append("</ol></body></html>");

    var objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType("text/html");
    var putRequest = new PutObjectRequest("jgmp", "index.html",
        new ByteArrayInputStream(htmlBuilder.toString().getBytes()), objectMetadata);
    s3client.putObject(putRequest);

    return null;
  }
}