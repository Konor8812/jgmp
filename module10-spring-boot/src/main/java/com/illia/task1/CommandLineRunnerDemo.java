package com.illia.task1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandLineRunnerDemo {
  @Bean
  CommandLineRunner runner(){
    return (args) -> System.out.println("hello world");
  }
}
