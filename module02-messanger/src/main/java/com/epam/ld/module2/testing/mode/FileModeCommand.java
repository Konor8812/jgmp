package com.epam.ld.module2.testing.mode;

import com.epam.ld.module2.testing.Client;
import com.epam.ld.module2.testing.Messenger;
import com.epam.ld.module2.testing.template.Template;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.regex.Pattern;

public class FileModeCommand implements Command {

  private final HashMap<String, String> knownPlaceholders = new HashMap<>();
  private final Path inputFile;
  private final Path outputFile;
  private final Messenger messenger;

  public FileModeCommand(Messenger messenger, Path inputFile, Path outputFile) {
    this.messenger = messenger;
    this.inputFile = inputFile;
    this.outputFile = outputFile;
  }

  @Override
  public void execute() {
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile.toFile()));
        var writer = new FileWriter(outputFile.toFile())) {

      var placeholderPattern = Pattern.compile("(#\\{.+})=(.+)");

      String line;
      while (!(line = reader.readLine()).equals("\\\\")) {
        var matcher = placeholderPattern.matcher(line);
        if (matcher.find()) {
          knownPlaceholders.put(matcher.group(1), matcher.group(2));
        }
      }

      var client = new Client();
      var addressBuilder = new StringBuilder();
      while (!(line = reader.readLine()).equals("\\\\")) {
        addressBuilder.append(line).append(System.lineSeparator());
      }
      client.setAddresses(addressBuilder.toString());

      var template = new Template();
      var messageBuilder = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        messageBuilder.append(line).append(System.lineSeparator());
      }
      template.setGreeting(messageBuilder.toString());

      messenger.sendMessage(client, template);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public HashMap<String, String> getKnownPlaceholders() {
    return knownPlaceholders;
  }
}
