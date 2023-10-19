package com.illia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestsApp {

  public static void main(String[] args) {
    var url = "http://jgmp-895833085.eu-north-1.elb.amazonaws.com";

    try {
      for (int i = 0; i < 10; i++) {
        var urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
        reader.close();

        System.out.println("Request " + (i + 1) + ": Response Code - " + connection.getResponseCode() + " Response: " + response);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}