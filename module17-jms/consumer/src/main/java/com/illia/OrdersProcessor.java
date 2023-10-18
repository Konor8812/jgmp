package com.illia;

import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class OrdersProcessor {

  private static final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
      "tcp://localhost:61616", "admin", "admin");

  public static void main(String[] args) {
    try (Connection connection = connectionFactory.createConnection()) {
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      var ordersDestination = session.createTopic("ordersTopic");

      String uncountablesSelector = "containsUncountableProducts = true";
      String countablesSelector = "containsUncountableProducts = false";

      var countablesConsumer = session.createConsumer(ordersDestination, countablesSelector);
      var uncountablesConsumer = session.createConsumer(ordersDestination, uncountablesSelector);

      countablesConsumer.setMessageListener(createMessageListener(session, false));
      uncountablesConsumer.setMessageListener(createMessageListener(session, true));
      connection.start();
      try {
        // Sleep indefinitely to prevent app from shutting down.
        Thread.sleep(Long.MAX_VALUE);
      } catch (InterruptedException e) {
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static MessageListener createMessageListener(Session session, boolean forCountables)
      throws JMSException {
    var rejectedDestination = session.createQueue("rejected");
    var acceptedDestination = session.createQueue("accepted");
    var rejectedProducer = session.createProducer(rejectedDestination);
    var acceptedProducer = session.createProducer(acceptedDestination);
    return (message) -> {
      try {
        var order = deserializeMessage(message);
        for (var product : order.positions()) {
          var msg = session.createTextMessage();

          if (order.total() > 100) {
            // rejected
            try {
              msg.setText("Order for " + order.customerName()
                  + " was rejected because you can't order for more than 100 units total");
              rejectedProducer.send(msg);
            } catch (JMSException e) {
              throw new RuntimeException(e);
            }
            break;
          }

          if (product.amount() > 50 && !forCountables) {
            //rejected
            try {
              msg.setText("Order for " + order.customerName()
                  + " was rejected because you can't order more that 50 units per uncountable product!");
              rejectedProducer.send(msg);
            } catch (JMSException e) {
              throw new RuntimeException(e);
            }
            break;
          }

          //accepted
          msg.setText("Order for " + order.customerName() + " was accepted");
          acceptedProducer.send(msg);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    };
  }

  private static Order deserializeMessage(Message message) throws JMSException {
    var messageContent = ((TextMessage) message).getText();

    var orderPattern = Pattern.compile("customerName=(.+), positions=\\[(.+)\\], total=(.+)\\)");
    var productPattern = Pattern.compile(
        "name=(.+), price=(.+), isCountable=(true|false), amount=(.+)\\)");

    var matcher = orderPattern.matcher(messageContent);

    Order order = null;
    if (matcher.find()) {

      var products = new ArrayList<Product>();
      var productMatcher = productPattern.matcher(matcher.group(2));
      while (productMatcher.find()) {
        products.add(
            new Product(productMatcher.group(1),
                Double.parseDouble(productMatcher.group(2)),
                Boolean.parseBoolean(productMatcher.group(3)),
                Double.parseDouble(productMatcher.group(4))));
      }
      order = new Order(matcher.group(1), products, Double.parseDouble(matcher.group(3)));
    }
    return order;
  }
}
