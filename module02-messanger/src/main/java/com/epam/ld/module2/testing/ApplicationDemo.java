package com.epam.ld.module2.testing;

import java.util.HashMap;

public class ApplicationDemo {

  private static ApplicationMode applicationMode;
  private static String[] args;
  private static final HashMap<String, String> knownPlaceholders = new HashMap<>();
  public static void main(String[] args) {
    if (args.length == 0) {
      applicationMode = ApplicationMode.CONSOLE;
    } else if (args.length == 2) {
      applicationMode = ApplicationMode.FILE;
    } else {
      throw new ApplicationException("Invalid args amount");
    }
    ApplicationDemo.args = args;
  }

  public static void start(){
    switch (applicationMode){
      case CONSOLE -> startInConsoleMode();
      case FILE -> startInFileMode();
      default -> throw new ApplicationException("Application mode not specified");
    }
  }

  private static void startInConsoleMode() {

  }
  private static void startInFileMode() {

  }

  public static ApplicationMode getApplicationMode() {
    return applicationMode;
  }

  public static HashMap<String, String> getKnownPlaceholders() {
    return knownPlaceholders;
  }
}
