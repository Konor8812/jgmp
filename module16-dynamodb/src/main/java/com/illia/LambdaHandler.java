package com.illia;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class LambdaHandler implements RequestHandler<Map<String, Object>, Void> {

  @Override
  public Void handleRequest(Map<String, Object> input, Context context) {

    byte[] page;
    var s3client = AmazonS3Manager.getS3Client();
    try {
      var getObjectRequest = new GetObjectRequest("jgmp", "index.html");
      page = s3client.getObject(getObjectRequest).getObjectContent().readAllBytes();
    } catch (IOException ex) {
      var resultPage = createPageFromDatabaseContent();
      return uploadPage(s3client, resultPage);
    }

    var inputAsList = (List) input.get("Records");

    Map<String, Model> modifiedModels = new HashMap<>();
    List<Model> insertedModels = new ArrayList<>();
    List<String> removedModelsKeys = new ArrayList<>();

    for (var record : inputAsList) {
      var content = (LinkedHashMap) record;
      var dynamodb = (LinkedHashMap) content.get("dynamodb");

      switch (content.get("eventName").toString().toUpperCase()) {
        case "INSERT" -> insertedModels.add(parseNewModel(dynamodb));
        case "MODIFY" -> {
          var model = parseNewModel(dynamodb);
          modifiedModels.put(model.getModel_name(), model);
        }
        case "REMOVE" -> removedModelsKeys.add(parseRemovedModel(dynamodb));
        default -> {
          var resultPage = createPageFromDatabaseContent();
          return uploadPage(s3client, resultPage);
        }
      }
    }

    var pageAsString = new String(page);
    var rowMatcher = Pattern.compile(
            "<li>Model\\(model_name=([a-zA-Z0-9 _]+), price=([0-9]+), picture_url=([a-zA-Z0-9]+)\\)<\\/li>")
        .matcher(pageAsString);

    List<Model> resultsList = new ArrayList<>();

    while (rowMatcher.find()) {
      var model = new Model();

      var model_name = rowMatcher.group(1);

      if (removedModelsKeys.contains(model_name)) {
        removedModelsKeys.remove(model_name);
        continue;
      }
      if (modifiedModels.containsKey(model_name)) {
        resultsList.add(modifiedModels.get(model_name));
        modifiedModels.remove(model_name);
        continue;
      }
      model.setModel_name(model_name);
      model.setPrice(Long.parseLong(rowMatcher.group(2)));
      model.setPicture_url(rowMatcher.group(3));
      resultsList.add(model);
    }
    resultsList.addAll(insertedModels);

    var resultPage = createPageFromCollection(resultsList);
    return uploadPage(s3client, resultPage);

  }

  private String parseRemovedModel(LinkedHashMap dynamodb) {

    var oldImage = (LinkedHashMap
        <?, LinkedHashMap
            <?, LinkedHashMap<?, ?>>>) dynamodb.get("OldImage");

    return String.valueOf(
        oldImage
            .get("model_name")
            .get("S"));
  }

  private Model parseNewModel(LinkedHashMap dynamodb) {
    var newImage = (LinkedHashMap
        <?, LinkedHashMap
            <?, LinkedHashMap<?, ?>>>) dynamodb.get("NewImage");

    var model = new Model();
    model.setModel_name(
        String.valueOf(
            newImage
                .get("model_name")
                .get("S")));
    model.setPrice(Long.parseLong(
        String.valueOf(
            newImage
                .get("price")
                .get("N"))));
    model.setPicture_url(
        String.valueOf(
            newImage
                .get("picture_url")
                .get("S")));

    return model;
  }

  private String createPageFromDatabaseContent() {
    var dynamoDBMapper = AmazonDynamoDBManager.getDynamoDBMapper();
    var elements = dynamoDBMapper.scan(Model.class, new DynamoDBScanExpression());

    return createPageFromCollection(elements);
  }

  private String createPageFromCollection(Collection<?> elements) {
    var htmlBuilder = new StringBuilder();
    htmlBuilder.append(
        "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"><title>Models</title></head><body><h1>Available models:</h1><ol>");
    for (var model : elements) {
      htmlBuilder.append("<li>")
          .append(model);
      htmlBuilder
          .append("</li>");
    }
    htmlBuilder.append("</ol></body></html>");

    return htmlBuilder.toString();
  }

  private Void uploadPage(AmazonS3 s3client, String resultPage) {
    var objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType("text/html");
    var putRequest = new PutObjectRequest("jgmp", "index.html",
        new ByteArrayInputStream(resultPage.getBytes()), objectMetadata);
    s3client.putObject(putRequest);
    return null;
  }

}