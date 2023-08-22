package com.illia;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
public class ApplicationDemo {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationDemo.class, args);
  }
}
