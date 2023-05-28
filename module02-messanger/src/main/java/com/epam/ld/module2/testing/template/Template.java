package com.epam.ld.module2.testing.template;

/**
 * The type Template.
 */
public class Template {

  private final String greeting;
  private final String ending;

  public String getGreeting() {
    return greeting;
  }

  public String getEnding() {
    return ending;
  }

  public Template(String greeting, String ending) {
    this.greeting = greeting;
    this.ending = ending;
  }
}
