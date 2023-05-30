package com.epam.ld.module2.testing.template;


import com.epam.ld.module2.testing.template.exception.TemplateEngineException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The type Template engine.
 */
public class TemplateEngine {

  private static final String placeholderPattern = "\\#\\{.+\\}";
  private final Map<String, String> placeholderValueMap;

  public TemplateEngine() {
    placeholderValueMap = new HashMap<>();
  }

  public void addPlaceholderReplacement(String placeholder, String value) {
    if (placeholder.matches(placeholderPattern)) {
      placeholderValueMap.put(placeholder, value);
    }
    // exception
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

  private String fillPlaceholders(String s) {
    if (s == null) {
      return ""; // makes sense
    }
    assertValuesForAllPlaceholdersProvided(s);

    for (var placeholder : placeholderValueMap.keySet()) {
      s = s.replaceAll(Pattern.quote(placeholder), placeholderValueMap.get(placeholder));
    }

    return s;
  }

  private void assertValuesForAllPlaceholdersProvided(String s) {
    var matcher = Pattern.compile(placeholderPattern).matcher(s);
    while (matcher.find()) {
      if (!placeholderValueMap.containsKey(matcher.group())) {
        throw new TemplateEngineException("No value provided for one or more placeholders");
      }
    }

  }
}
