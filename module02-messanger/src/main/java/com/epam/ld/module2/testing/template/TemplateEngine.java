package com.epam.ld.module2.testing.template;

import com.epam.ld.module2.testing.Client;
import java.util.regex.Pattern;

/**
 * The type Template engine.
 */
public class TemplateEngine {

  private final String senderName;
  private final String senderPlaceholderRegex;
  private final String recipientPlaceholderRegex;

  public TemplateEngine(String senderName, String senderPlaceholder, String recipientPlaceholder) {
    this.senderName = senderName;
    this.senderPlaceholderRegex = Pattern.quote(senderPlaceholder);
    this.recipientPlaceholderRegex = Pattern.quote(recipientPlaceholder);
  }

  /**
   * Generate message string.
   *
   * @param template the template
   * @param client   the client
   * @return the string
   */
  public String generateMessage(Template template, Client client) {
    var modifiedGreeting = template.getGreeting()
        .replaceAll(senderPlaceholderRegex, senderName)
        .replaceAll(recipientPlaceholderRegex, client.getName());
    var modifiedEnding = template.getEnding()
        .replaceAll(senderPlaceholderRegex, senderName)
        .replaceAll(recipientPlaceholderRegex, client.getName());

    return modifiedGreeting
        + "\n#{content}\n"
        + modifiedEnding;
  }
}
