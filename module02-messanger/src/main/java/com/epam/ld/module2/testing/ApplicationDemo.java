package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    var messenger = new Messenger(new MailServer(), new TemplateEngine());
    try(var reader = new BufferedReader(new InputStreamReader(System.in));
        var writer = System.out){
      writer.println("Enter placeholders, double backslash \"\\\\\" to end");
      String key;
      while(!(key = reader.readLine()).equals("\\\\")){
        var value = reader.readLine();
        knownPlaceholders.put(key, value);
      }
      writer.println("Enter addresses");
      var client = new Client();
      client.setAddresses(reader.readLine());
      writer.println("Enter message with placeholders");
      var template = new Template();
      template.setGreeting(reader.readLine());
      messenger.sendMessage(client, template);
    }catch (IOException e){
      e.printStackTrace();
    }
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
