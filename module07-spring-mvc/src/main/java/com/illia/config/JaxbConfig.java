package com.illia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class JaxbConfig {

  @Bean
  public Jaxb2Marshaller jaxb2Marshaller() {
    var jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setPackagesToScan("com.illia.model");
    return jaxb2Marshaller;
  }
}
