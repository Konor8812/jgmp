package com.epam.ld.module2.testing.template;

import static com.epam.ld.module2.testing.constants.TestConstants.NO_VALUE_FOR_PLACEHOLDER_EXCEPTION_MESSAGE;
import static com.epam.ld.module2.testing.constants.TestConstants.RECIPIENT_NAME;
import static com.epam.ld.module2.testing.constants.TestConstants.RECIPIENT_PLACEHOLDER;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_NAME;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.ld.module2.testing.template.exception.TemplateEngineException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TemplateEngineTest {

  TemplateEngine templateEngine = new TemplateEngine();

  @AfterEach
  public void cleanPlaceholderMap() {
    templateEngine.cleanPlaceholdersValues();
  }

  @Test
  public void templateEngineTestShouldReplacePlaceholderInGreeting() {
    var greeting = "Dear %s";
    var templateGreeting = String.format(greeting, RECIPIENT_PLACEHOLDER);
    templateEngine.addPlaceholderReplacement(RECIPIENT_PLACEHOLDER, RECIPIENT_NAME);

    var template = new Template();
    template.setGreeting(templateGreeting);

    var message = templateEngine.generateMessage(template);
    var split = message.split("\\n");

    var expectedGreeting = String.format(greeting, RECIPIENT_NAME);
    assertEquals(expectedGreeting, split[0]);
  }

  @Test
  public void templateEngineTestShouldReplacePlaceholderInEnding() {
    var ending = "Sincerely yours, %s";
    var templateEnding = String.format(ending, SENDER_PLACEHOLDER);
    templateEngine.addPlaceholderReplacement(SENDER_PLACEHOLDER, SENDER_NAME);

    var template = new Template();
    template.setEnding(templateEnding);

    var message = templateEngine.generateMessage(template);
    var split = message.split("\\n");

    var expectedEnding = String.format(ending, SENDER_NAME);
    assertEquals(expectedEnding, split[split.length - 1]);
  }

  @Test
  public void templateEngineTestShouldThrowExceptionIfNoValueForPlaceholderProvided() {
    var greeting = "Dear %s";
    var templateGreeting = String.format(greeting, RECIPIENT_PLACEHOLDER);

    var template = new Template();
    template.setGreeting(templateGreeting);

    var ex = assertThrows(TemplateEngineException.class,
        () -> templateEngine.generateMessage(template));
    assertEquals(NO_VALUE_FOR_PLACEHOLDER_EXCEPTION_MESSAGE, ex.getMessage());
  }

  @Test
  public void templateEngineTestShouldReplacePlaceholderWithAnotherPlaceholder() {
    var greeting = "Dear %s";
    var templateGreeting = String.format(greeting, RECIPIENT_PLACEHOLDER);
    templateEngine.addPlaceholderReplacement(RECIPIENT_PLACEHOLDER, SENDER_PLACEHOLDER);

    var template = new Template();
    template.setGreeting(templateGreeting);

    var message = templateEngine.generateMessage(template);
    var split = message.split("\\n");

    var expectedGreeting = String.format(greeting, SENDER_PLACEHOLDER);
    assertEquals(expectedGreeting, split[0]);
  }
}
