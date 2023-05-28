package com.epam.ld.module2.testing.template;

import static com.epam.ld.module2.testing.constants.TestConstants.INVALID_PLACEHOLDER;
import static com.epam.ld.module2.testing.constants.TestConstants.INVALID_RECIPIENT_PLACEHOLDER_EXCEPTION;
import static com.epam.ld.module2.testing.constants.TestConstants.INVALID_SENDER_PLACEHOLDER_EXCEPTION;
import static com.epam.ld.module2.testing.constants.TestConstants.RECIPIENT_NAME;
import static com.epam.ld.module2.testing.constants.TestConstants.RECIPIENT_PLACEHOLDER;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_NAME;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.ld.module2.testing.Client;
import com.epam.ld.module2.testing.template.exception.TemplateEngineException;
import org.junit.jupiter.api.Test;

public class TemplateEngineTest {

  TemplateEngine templateEngine = new TemplateEngine(SENDER_NAME,
      SENDER_PLACEHOLDER,
      RECIPIENT_PLACEHOLDER);


  @Test
  public void templateEngineTestShouldReplacePlaceholdersWithProvidedValues() {
    var greeting = "Dear %s";
    var ending = "Sincerely yours, %s";
    var templateGreeting = String.format(greeting, RECIPIENT_PLACEHOLDER);
    var templateEnding = String.format(ending, SENDER_PLACEHOLDER);
    var template = new Template(templateGreeting, templateEnding);

    var client = new Client();
    client.setName(RECIPIENT_NAME);

    var message = templateEngine.generateMessage(template, client);
    var split = message.split("\\n");

    var expectedGreeting = String.format(greeting, RECIPIENT_NAME);
    var expectedEnding = String.format(ending, SENDER_NAME);
    assertEquals(expectedGreeting, split[0]);
    assertEquals(expectedEnding, split[split.length - 1]);
  }

  @Test
  public void templateEngineTestShouldThrowExceptionIfNoValidRecipientPlaceholdersProvided() {
    var greeting = "Dear %s";
    var ending = "Sincerely yours, %s";
    var templateGreeting = String.format(greeting, INVALID_PLACEHOLDER);
    var templateEnding = String.format(ending, SENDER_PLACEHOLDER);
    var template = new Template(templateGreeting, templateEnding);

    var client = new Client();
    client.setName(RECIPIENT_NAME);

    var ex = assertThrows(TemplateEngineException.class,
        () -> templateEngine.generateMessage(template, client));
    assertEquals(INVALID_RECIPIENT_PLACEHOLDER_EXCEPTION, ex.getMessage());
  }

  @Test
  public void templateEngineTestShouldThrowExceptionIfNoValidSenderPlaceholdersProvided() {
    var greeting = "Dear %s";
    var ending = "Sincerely yours, %s";
    var templateGreeting = String.format(greeting, RECIPIENT_PLACEHOLDER);
    var templateEnding = String.format(ending, INVALID_PLACEHOLDER);
    var template = new Template(templateGreeting, templateEnding);

    var client = new Client();
    client.setName(RECIPIENT_NAME);

    var ex = assertThrows(TemplateEngineException.class,
        () -> templateEngine.generateMessage(template, client));
    assertEquals(INVALID_SENDER_PLACEHOLDER_EXCEPTION, ex.getMessage());
  }
}
