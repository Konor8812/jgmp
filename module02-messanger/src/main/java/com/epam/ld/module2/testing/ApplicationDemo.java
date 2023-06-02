package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.mode.Command;
import com.epam.ld.module2.testing.mode.ConsoleModeCommand;
import com.epam.ld.module2.testing.mode.FileModeCommand;
import java.nio.file.Path;

public class ApplicationDemo {

  private static ApplicationMode applicationMode;
  private static String[] args;
  private static Command command;

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

  public static void start(Messenger messenger) {
    if (messenger == null) {
      throw new ApplicationException("No messenger provided");
    }
    switch (applicationMode) {
      case CONSOLE -> command = new ConsoleModeCommand(messenger);
      case FILE -> command = new FileModeCommand(messenger, Path.of(args[0]), Path.of(args[1]));
      default -> throw new ApplicationException("Application mode not specified");
    }
    command.execute();
  }

  public static void setApplicationMode(ApplicationMode applicationMode) {
    ApplicationDemo.applicationMode = applicationMode;
  }

  public static void setArgs(String[] args) {
    ApplicationDemo.args = args;
  }

  public static ApplicationMode getApplicationMode() {
    return applicationMode;
  }

  public static Command getCommand() {
    return command;
  }
}
