package com.illia;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;


public class OrdersProducer {

  private static final BufferedReader consoleReader = new BufferedReader(
      new InputStreamReader(System.in));
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
      "tcp://localhost:61616", "admin", "admin");

  public static void main(String[] args) {
    boolean appRunning = true;

    while (appRunning) {
      printMessage(
          "Please make your order: \n 1) Proceed \n 2) Exit");
      String operation = readLine("");
      while (!validOperation(operation)) {
        operation = readLine(
            "Operation is not supported, please do \n 1) Proceed \n 2) Exit");
      }

      if (operation.equals("1")) {
        createNewOrder();
      } else {
        appRunning = false;
      }
    }
  }

  private static boolean containsUncountableProducts = false;

  private static void createNewOrder() {
    var name = readLine("What is your name?");
    while (!(isNameValid(name))) {
      name = readLine("What is your name?");
    }
    Order order = new Order(name);

    while (true) {
      var line = readLine("Add another product? \n 1) Yes \n 2) No");
      if (line.equals("1")) {
        addNewPosition(order);
      } else if (line.equals("2")) {
        break;
      }
    }

    sendOrder(order);
  }

  private static boolean isNameValid(String name) {
    return name.matches("[a-zA-Z]{2,}");
  }

  private static void addNewPosition(Order order) {
    var productName = readLine("Enter product");

    while (productName == null) {
      productName = readLine("Enter product");
    }

    var price = productName.length() * 2;
    printMessage("This costs  " + price);

    var isCountableInput = readLine("Is it countable? Y\\N").toUpperCase();
    boolean isCountable;
    while (true) {
      if (isCountableInput.equals("Y")) {
        isCountable = true;
        break;
      } else if (isCountableInput.equals("N")) {
        isCountable = false;
        containsUncountableProducts = true;
        break;
      }
      isCountableInput = readLine("Is it countable? Y\\N").toUpperCase();
    }

    String amountInput;
    Number amount;

    while ((amount = isCountable ?
        tryParseInteger(readLine("Enter amount"))
        : tryParseDecimal(readLine("Enter volume"))) == null) {
    }

    order.addPosition(new Product(productName,
        price,
        isCountable,
        isCountable? amount.longValue() : amount.doubleValue()));

  }

  private static void sendOrder(Order order) {
    sendMessage(order.toString());
  }

  private static void sendMessage(String msg) {
    try (Connection connection = connectionFactory.createConnection()) {
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Destination destination = session.createTopic("ordersTopic");
      MessageProducer producer = session.createProducer(destination);
      var message = session.createTextMessage();
      message.setText(objectMapper.writeValueAsString(msg));

      message.setBooleanProperty("containsUncountableProducts", containsUncountableProducts);

      producer.send(message);
      printMessage("Your order was sent further!");
    } catch (Exception ex) {
      ex.printStackTrace();
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

  private static void printMessage(String message) {
    if (!message.isBlank()) {
      System.out.println(message);
    }
  }

  private static final Pattern decimalPattern = Pattern.compile("\\d+\\.\\d+");

  private static Double tryParseDecimal(String input) {
    if (decimalPattern.matcher(input).matches()) {
      return Double.parseDouble(input);
    }
    return null;
  }

  private static final Pattern integerPattern = Pattern.compile("\\d+");

  private static Long tryParseInteger(String input) {
    if (integerPattern.matcher(input).matches()) {
      return Long.parseLong(input);
    }
    return null;
  }

  private static boolean validOperation(String input) {
    return input.equals("1") || input.equals("2");
  }
}
