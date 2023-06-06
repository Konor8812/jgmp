package com.epam.ld.module2.testing;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mockito;

public class MailServerTest {

  MailServer mailServer = new MailServer();

  @TestFactory
  Stream<DynamicTest> dynamicTestsWithStreamExample() {
    return Stream.of("msg1", "msg2")
        .map(msg -> dynamicTest("Test for " + msg, () -> {
          assertDoesNotThrow(() -> mailServer.send("address", msg));
        }));
  }


  @Test
  public void testPartialMock() {
    var mailServer = Mockito.mock(MailServer.class, CALLS_REAL_METHODS);

    when(mailServer.sendFast(any(), any()))
        .thenReturn("mocked");

    String result1 = mailServer.sendFast("", "");

    String result2 = mailServer.sendInstant("", "");

    assertEquals("mocked", result1);
    assertEquals("sent instant", result2); // real implementation
  }

}
