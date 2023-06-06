package com.epam.ld.module2.testing;

/**
 * Mail server class.
 */
public class MailServer {

  /**
   * Send notification.
   *
   * @param addresses      the addresses
   * @param messageContent the message content
   */
  public void send(String addresses, String messageContent) {

  }

  public String sendFast(String addresses, String messageContent) {
    return "sent fast";
  }

  public String sendInstant(String addresses, String messageContent) {
    return "sent instant";
  }

}
