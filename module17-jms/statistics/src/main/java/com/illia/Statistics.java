package com.illia;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

  private static final String filename = "logs.txt";
  private static final String filenameLocal = "logsLocal.txt";
  private static void writeChanges(String row) {
    try {
      Files.delete(Path.of(filename));
      Files.delete(Path.of(filenameLocal));
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      downloadFile();

    } catch (Exception e) {
      try {
        Files.createFile(Path.of(filename));
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }

    boolean recordExists = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(filename));
        BufferedWriter writer = new BufferedWriter(new FileWriter(filenameLocal, true))) {

      while (reader.ready()) {
        var line = reader.readLine();

        if (line.startsWith(row)) {
          var split = line.split("=");
          var updatedValue = Long.parseLong(split[1]) + 1;
          line = row + updatedValue;
          recordExists = true;
        }
        writer.write(line);
      }

      if(!recordExists) {
      writer.write(row + 1 + System.lineSeparator());
      }
      writer.flush();
      uploadFile(Path.of(filenameLocal).toFile());
    } catch (IOException ex) {
      ex.printStackTrace();
    }



  }

  private static void downloadFile() throws IOException {
    String bucketName = "jgmp";
    var s3client = AmazonS3Manager.getS3Client();

    var s3Object = s3client.getObject(new GetObjectRequest(bucketName, filename));
    s3Object.getObjectContent().transferTo(new FileOutputStream(filename));
  }

  private static void uploadFile(File file) {
    System.out.println(file.getName());
    var s3client = AmazonS3Manager.getS3Client();
    var objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType("text/plain");
    var putRequest = new PutObjectRequest("jgmp", filename, file);
    s3client.putObject(putRequest);
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
