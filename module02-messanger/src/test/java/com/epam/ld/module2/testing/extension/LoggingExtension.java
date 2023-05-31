package com.epam.ld.module2.testing.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;

public class LoggingExtension implements TestWatcher, TestExecutionExceptionHandler {

  @Override
  public void testSuccessful(ExtensionContext context) {
    System.out.println("Test passed " + context.getDisplayName());
  }

  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    System.out.println("Test aborted " + context.getDisplayName());
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    System.out.println("Test failed " + context.getDisplayName());
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
      throws Throwable {
    System.out.println("Test " + context.getDisplayName() + " execution caused exception "
        + throwable.getMessage());
  }
}