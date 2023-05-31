package com.epam.ld.module2.testing;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class MailServerTest {

  MailServer mailServer = new MailServer();

  @TestFactory
  Stream<DynamicTest> dynamicTestsWithStreamExample() {
    return Stream.of("msg1", "msg2")
        .map(msg -> dynamicTest("Test for " + msg, () -> {
          assertDoesNotThrow(() -> mailServer.send("address", msg));
        }));
  }

}
