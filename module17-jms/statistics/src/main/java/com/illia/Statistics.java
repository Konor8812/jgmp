package com.illia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import lombok.SneakyThrows;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Statistics {

  private static final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
      "tcp://localhost:61616", "admin", "admin");

  public static void main(String[] args) throws Exception {
//    Connection connection = connectionFactory.createConnection();
    try (Connection connection = connectionFactory.createConnection()) {
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      var acceptedDestination = session.createQueue("accepted");
      var rejectedDestination = session.createQueue("rejected");

      var acceptedConsumer = session.createConsumer(acceptedDestination);
      var rejectedConsumer = session.createConsumer(rejectedDestination);

      acceptedConsumer.setMessageListener((message) -> {
            var name = getCustomerName(message);
            if (name != null) {
              writeChanges(name + ":accepted=");
            }

          }
      );

      rejectedConsumer.setMessageListener((message) -> {
        var name = getCustomerName(message);
        if (name != null) {
          writeChanges(name + ":rejected=");
        }
      });

      connection.start();
      try {
        Thread.sleep(Long.MAX_VALUE); // Sleep indefinitely.
      } catch (InterruptedException e) {
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }


  private static void writeChanges(String row) {

    var path = Path.of("logs.txt");

    if(Files.exists(path)){
      System.out.println("file exists");
      boolean recordExists = false;
      var lines = new ArrayList<String>();
      try (BufferedReader reader = new BufferedReader(new FileReader("logs.txt"))) {
        while (reader.ready()) {
          var line = reader.readLine();
          if (line.startsWith(row)) {
            var split = line.split("=");
            var updatedValue = Long.parseLong(split[1]) + 1;
            line = row + updatedValue;
            recordExists = true;
          }
          lines.add(line);
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      try {
        Files.delete(path);
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("content to write " + lines);
      try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs.txt", true))) {

        for (var line : lines) {
          writer.write(line + System.lineSeparator());
        }
        if (!recordExists){
          writer.write(row + 1 + System.lineSeparator());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }else {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs.txt", true))) {
        writer.write(row + 1 + System.lineSeparator());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }


  }

  private static final Pattern messagePattern = Pattern.compile("Order for ([a-zA-Z]+) ");

  @SneakyThrows
  private static String getCustomerName(Message message) {
    var messageContent = ((TextMessage) message).getText();
    var matcher = messagePattern.matcher(messageContent);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }
}
