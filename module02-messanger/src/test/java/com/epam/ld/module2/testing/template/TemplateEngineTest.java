package com.epam.ld.module2.testing.template;

import static com.epam.ld.module2.testing.constants.TestConstants.RECIPIENT_NAME;
import static com.epam.ld.module2.testing.constants.TestConstants.RECIPIENT_PLACEHOLDER;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_NAME;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.epam.ld.module2.testing.Client;
import org.junit.jupiter.api.Test;

public class TemplateEngineTest {

  TemplateEngine templateEngine = new TemplateEngine(SENDER_NAME,
      SENDER_PLACEHOLDER,
      RECIPIENT_PLACEHOLDER);


  @Test
  public void templateEngineShouldReplacePlaceholdersWithProvidedValues() {
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
}
