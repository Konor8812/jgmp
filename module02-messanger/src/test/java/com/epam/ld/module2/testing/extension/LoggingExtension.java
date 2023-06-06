package com.epam.ld.module2.testing.extension;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class LoggingExtension implements TestWatcher {

  private static final String OUTPUT_FILE = "execution_info.txt";

  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE, true))) {
      writer.println("Test aborted " + context.getDisplayName());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    System.out.println("hh");
    try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE, true))) {
      writer.println("Test failed " + context.getDisplayName());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}