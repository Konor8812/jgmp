package com.epam.ld.module2.testing.mode;

import com.epam.ld.module2.testing.Client;
import com.epam.ld.module2.testing.Messenger;
import com.epam.ld.module2.testing.template.Template;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ConsoleModeCommand implements Command {

  private final HashMap<String, String> knownPlaceholders = new HashMap<>();
  private final Messenger messenger;

  public ConsoleModeCommand(Messenger messenger) {
    this.messenger = messenger;
  }

  @Override
  public void execute() {
    try (var reader = new BufferedReader(new InputStreamReader(System.in));
        var writer = System.out) {
      writer.println("Enter placeholders, double backslash \"\\\\\" to end");
      String key;
      while (!(key = reader.readLine()).equals("\\\\")) {
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public HashMap<String, String> getKnownPlaceholders() {
    return knownPlaceholders;
  }
}
