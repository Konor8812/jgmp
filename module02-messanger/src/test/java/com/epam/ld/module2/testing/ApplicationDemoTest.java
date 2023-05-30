package com.epam.ld.module2.testing;

import static com.epam.ld.module2.testing.ApplicationMode.CONSOLE;
import static com.epam.ld.module2.testing.ApplicationMode.FILE;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_NAME;
import static com.epam.ld.module2.testing.constants.TestConstants.SENDER_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
  String inputFilename = "fileWithPlaceholders.properties";
  String outputFilename = "output.txt";


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

    var data = String.format("""
        %s
        %s
        \\""", SENDER_PLACEHOLDER, SENDER_NAME).getBytes(StandardCharsets.UTF_8);

    ApplicationDemo.main(new String[]{});
    ApplicationDemo.start();

    try (var inputStream = new ByteArrayInputStream(data);
        var byteOs = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteOs)) {
      // Imitation of console input and output
      System.setIn(inputStream);
      System.setOut(printStream);

      // ensures data was written in System.out
      System.out.print(new String(inputStream.readAllBytes()));
      assertArrayEquals(data, byteOs.toByteArray());

      // ensures data from imitated console input was received and parsed
      assertEquals(1, ApplicationDemo.getKnownPlaceholders().size());
      assertEquals(SENDER_NAME, ApplicationDemo.getKnownPlaceholders().get(SENDER_PLACEHOLDER));

    } finally {
      System.setIn(defaultIn);
      System.setOut(defaultOut);
    }
  }

  @Test
  public void consoleModeShouldReadFromFileSystem() throws IOException {
    var data = String.format("""
        %s
        %s
        \\""", SENDER_PLACEHOLDER, SENDER_NAME).getBytes(StandardCharsets.UTF_8);

    prepareFiles(data);

    ApplicationDemo.main(new String[]{inputFilename, outputFilename});
    ApplicationDemo.start();

    assertEquals(1, ApplicationDemo.getKnownPlaceholders().size());
    assertEquals(SENDER_NAME, ApplicationDemo.getKnownPlaceholders().get(SENDER_PLACEHOLDER));

  }



  private void prepareFiles(byte[] inputFileContent) throws IOException {
    var file = tempDir.resolve(inputFilename);
    Files.createFile(file);
    Files.createFile(tempDir.resolve(outputFilename));
    try (OutputStream outputStream = new FileOutputStream(file.toFile())) {
      outputStream.write(inputFileContent);
      outputStream.flush();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
