package com.epam.ld.module2.testing;

import static com.epam.ld.module2.testing.ApplicationMode.CONSOLE;
import static com.epam.ld.module2.testing.ApplicationMode.FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ApplicationDemoTest {

  @Test
  public void applicationShouldStartInConsoleModeIfNoArgsPassed() {
    ApplicationDemo.main(new String[]{});
    assertEquals(CONSOLE, ApplicationDemo.getApplicationMode());
  }

  @Test
  public void applicationShouldStartInFileModeTwoArgsPassed() {
    ApplicationDemo.main(new String[]{"", ""});
    assertEquals(FILE, ApplicationDemo.getApplicationMode());
  }

  @Test
  public void applicationStartShouldThrowExceptionIfInvalidArgsNumber() {
    assertThrows(ApplicationException.class, () -> ApplicationDemo.main(new String[]{"", "", ""}));
  }
}
