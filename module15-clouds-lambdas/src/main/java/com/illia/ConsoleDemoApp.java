package com.illia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConsoleDemoApp {

  private static boolean appRunning = false;
  private static final BufferedReader consoleReader = new BufferedReader(
      new InputStreamReader(System.in));

  public static void main(String[] args) {
    appRunning = true;

    while (appRunning) {
      printMessage(
          "Enter desired operation: \n 1) Create new model \n 2) Update existing model\n 3) exit");
      String operation = readLine("");
      while (!validOperation(operation)) {
        operation = readLine(
            "Operation is not supported, please do \n 1) Create new model \n 2) Update existing model\n 3) exit");
      }

      if (operation.equals("1")) {
        proceedUploadOperation();
      } else if (operation.equals("2")) {
        proceedUpdateOperation();
      } else {
        appRunning = false;
      }
    }
  }

  private static void proceedUpdateOperation() {
    sendRequest("PUT", buildBody());
  }

  private static void proceedUploadOperation() {
    sendRequest("POST", buildBody());
  }

  private static String buildBody() {
    String model_name;
    long price;
    String picture_url;

    model_name = readLine("Enter model name");
    while (!isModelNameValid(model_name)) {
      model_name = readLine("Model name should be not blank");
    }

    price = tryParsePrice(readLine("Enter model price, optional, 0 if invalid input"));

    picture_url = readLine("Enter picture url, optional");

    return String.format("{\"model_name\":\"%s\",\"price\":%d,\"picture_url\":\"%s\"}",
        model_name, price, picture_url);
  }



  private static void sendRequest(String method, String body) {
    try {
      URL url = new URL(
          "https://lwchnzdtxl.execute-api.eu-north-1.amazonaws.com/deployment/models");

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method);

      connection.setDoInput(true);
      connection.setDoOutput(true);

      try (OutputStream os = connection.getOutputStream()) {
        os.write(body.getBytes());
      }

      int responseCode = connection.getResponseCode();

      try (BufferedReader br = new BufferedReader(
          new InputStreamReader(connection.getInputStream()))) {
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null) {
          response.append(line);
        }

        printMessage("\nResponse Code: " + responseCode);
        printMessage("\nResponse Data: " + response);
      }
      connection.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private static String readLine(String message) {
    try {
      printMessage(message);
      return consoleReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  private static long tryParsePrice(String priceInput) {
    try {
      return Long.parseLong(priceInput);
    }catch (Exception ex){
      return 0;
    }
  }
  private static void printMessage(String message) {
    if (!message.isBlank()) {
      System.out.println(message);
    }
  }
  private static boolean isModelNameValid(String model_name) {
    return model_name != null && !model_name.isBlank();
  }

  private static boolean validOperation(String input) {
    return input.equals("1") || input.equals("2") || input.equals("3");
  }
}
