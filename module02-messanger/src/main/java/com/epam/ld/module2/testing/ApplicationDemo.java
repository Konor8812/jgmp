package com.epam.ld.module2.testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApplicationDemo {

  public static void main(String[] args) {
    if(args.length == 0){
      startInConsoleMode();
    } else if (args.length == 2) {
      startInFileMode(args[0], args[1]);
    }
  }

  private static void startInConsoleMode() {
    var mailServer = new MailServer();
    try(var reader = new BufferedReader(new InputStreamReader(System.in));
    var writer = System.out){

      String str;
      while(!(str = reader.readLine()).equals("\\")){

      }

    }catch (IOException e){

    }
  }

  private static void startInFileMode(String input, String output) {

  }
}
