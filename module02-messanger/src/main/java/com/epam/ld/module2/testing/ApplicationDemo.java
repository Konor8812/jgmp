package com.epam.ld.module2.testing;

public class ApplicationDemo {

  private static ApplicationMode applicationMode;
  private static String[] args;

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


  public static ApplicationMode getApplicationMode() {
    return applicationMode;
  }
}
