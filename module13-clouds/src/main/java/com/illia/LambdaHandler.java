package com.illia;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LambdaHandler implements RequestStreamHandler {

  @Override
  public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    String url = "http://jgmp-895833085.eu-north-1.elb.amazonaws.com";

    try {
      for (int i = 0; i < 10; i++) {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        String response = "Request " + (i + 1) + ": Response Code - " + responseCode;
        output.write(response.getBytes());

        connection.disconnect();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


