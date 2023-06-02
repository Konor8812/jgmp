package com.epam.ld.module2.testing.template;


import com.epam.ld.module2.testing.template.exception.TemplateEngineException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Template engine.
 */
public class TemplateEngine {

  private static final String placeholderPattern = "\\#\\{[\\x00-\\xFF]+\\}";
  private final Map<String, String> placeholderValueMap;

  public TemplateEngine() {
    placeholderValueMap = new HashMap<>();
  }

  public void addPlaceholderReplacement(String placeholder, String value) {
    if (placeholder.matches(placeholderPattern)) {
      placeholderValueMap.put(placeholder, value);
    } else {
      throw new TemplateEngineException("no for " + placeholder + "   value   " + value);
    }
  }

  public void cleanPlaceholdersValues() {
    placeholderValueMap.clear();
  }

  /**
   * Generate message string.
   *
   * @param template the template
   * @return the string
   */
  public String generateMessage(Template template) {
    var modifiedGreeting = fillPlaceholders(template.getGreeting());
    var modifiedEnding = fillPlaceholders(template.getEnding());

    return modifiedGreeting
        + "\n#{content}\n"
        + modifiedEnding;
  }

  private String fillPlaceholders(String stringWithPlaceholders) {
    if (stringWithPlaceholders == null) {
      return ""; // makes sense
    }
    ensureValuesForAllPlaceholdersProvided(stringWithPlaceholders);

    String response = stringWithPlaceholders;
    for (var placeholder : placeholderValueMap.keySet()) {
      var escapedSpecialChars = Matcher.quoteReplacement(placeholder);
      response = stringWithPlaceholders.replaceAll(Pattern.quote(escapedSpecialChars),
          placeholderValueMap.get(placeholder));
    }

    return response;
  }

  private void ensureValuesForAllPlaceholdersProvided(String stringWithPlaceholders) {
    var matcher = Pattern.compile(placeholderPattern).matcher(stringWithPlaceholders);
    while (matcher.find()) {
      if (!placeholderValueMap.containsKey(matcher.group())) {
        throw new TemplateEngineException("No value provided for one or more placeholders");
      }
    }

  }
}
