package com.illia;

import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class OrdersProcessor {

  private static final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
      "tcp://localhost:61616", "admin", "admin");

  public static void main(String[] args) throws Exception {
//    Connection connection = connectionFactory.createConnection();
    try (Connection connection = connectionFactory.createConnection()){
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      var ordersDestination = session.createQueue("orders");
      var rejectedDestination = session.createQueue("rejected");
      var acceptedDestination = session.createQueue("accepted");

      var consumer = session.createConsumer(ordersDestination);

      var rejectedProducer = session.createProducer(rejectedDestination);
      var acceptedProducer = session.createProducer(acceptedDestination);

      consumer.setMessageListener((message) -> {
            try {
              var order = deserializeMessage(message);
              for (var product : order.positions()) {
                var msg = session.createTextMessage();

                if (product.amount() > 50 && !product.isCountable()) {
                  //rejected
                  System.out.println("rejected 1");
                  try {
                    msg.setText("Order for " + order.customerName()
                        + " was rejected because you can't order more that 50 units per uncountable product!");
                    rejectedProducer.send(msg);
                  } catch (JMSException e) {
                    throw new RuntimeException(e);
                  }
                  break;
                } else if (product.price() < 4) {
                  // rejected
                  System.out.println("rejected 2");
                  try {
                    msg.setText("Order for " + order.customerName()
                        + " was rejected because you can't order for less than 10 units per position");
                    rejectedProducer.send(msg);
                  } catch (JMSException e) {
                    throw new RuntimeException(e);
                  }
                  break;
                }
                //accepted
                System.out.println("accepted");
                msg.setText("Order for " + order.customerName() + " was accepted");
                acceptedProducer.send(msg);
              }
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
      );
      connection.start();
      try {
        Thread.sleep(Long.MAX_VALUE); // Sleep indefinitely.
      } catch (InterruptedException e) {
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }

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
            new Product(productMatcher.group(1), Double.parseDouble(productMatcher.group(2)),
                Boolean.parseBoolean(productMatcher.group(3)),
                Double.parseDouble(productMatcher.group(4))));
      }
      order = new Order(matcher.group(1), products, Double.parseDouble(matcher.group(3)));
    }
    return order;
  }
}
