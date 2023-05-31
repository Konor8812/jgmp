package com.epam.ld.module2.testing;

import static com.epam.ld.module2.testing.ApplicationMode.CONSOLE;
import static com.epam.ld.module2.testing.ApplicationMode.FILE;
import static com.epam.ld.module2.testing.constants.TestConstants.CLIENT_ADDRESSES;
import static com.epam.ld.module2.testing.constants.TestConstants.MESSAGE_CONTENT;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_NAME;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ApplicationDemoTest {

  @TempDir
  Path tempDir;

  String defaultInputName = "input.txt";
  String inputFilename;
  String defaultOutputName = "output.txt";
  String outputFilename;


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

  @Test
  public void consoleModeShouldReadAndWriteToConsole() throws IOException {
    var defaultIn = System.in;
    var defaultOut = System.out;

    var data = String.format("%s%s%s%s\\\\", SENDER_PLACEHOLDER,
        System.lineSeparator(),
        SENDER_NAME,
        System.lineSeparator()).getBytes(StandardCharsets.UTF_8);

    try (var inputStream = new ByteArrayInputStream(data);
        var byteOs = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteOs)) {
      System.setIn(inputStream);
      System.setOut(printStream);

      ApplicationDemo.main(new String[]{});
      ApplicationDemo.start();

      // most likely needs to be refactored, to many assertions
      // ensures data from imitated console input was received and parsed
      assertEquals(1, ApplicationDemo.getKnownPlaceholders().size());
      assertEquals(SENDER_NAME, ApplicationDemo.getKnownPlaceholders().get(SENDER_PLACEHOLDER));

      // ensures data was written to System.out
      assertNotEquals(0, byteOs.toByteArray().length);
    } finally {
      System.setIn(defaultIn);
      System.setOut(defaultOut);
    }

  }

  @Test
  public void consoleModeShouldReadFromFileSystem() throws IOException {
    var data = String.format("%s=%s%s\\\\%s%s%s\\\\%s%s",
        SENDER_PLACEHOLDER,
        SENDER_NAME,
        System.lineSeparator(),
        System.lineSeparator(),
        CLIENT_ADDRESSES,
        System.lineSeparator(),
        System.lineSeparator(),
        MESSAGE_CONTENT).getBytes(StandardCharsets.UTF_8);

    prepareFiles(data);

    ApplicationDemo.main(new String[]{inputFilename, outputFilename});
    ApplicationDemo.start();

    assertEquals(1, ApplicationDemo.getKnownPlaceholders().size());
    assertEquals(SENDER_NAME, ApplicationDemo.getKnownPlaceholders().get(SENDER_PLACEHOLDER));

  }

  private void prepareFiles(byte[] inputFileContent) throws IOException {
    var inputFile = tempDir.resolve(defaultInputName);
    inputFilename = inputFile.toString();
    Files.createFile(inputFile);

    var outputFile = tempDir.resolve(defaultOutputName);
    inputFilename = inputFile.toString();
    Files.createFile(outputFile);
    outputFilename = outputFile.toString();

    try (OutputStream outputStream = new FileOutputStream(inputFile.toFile())) {
      outputStream.write(inputFileContent);
      outputStream.flush();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
