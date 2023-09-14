package com.illia.config;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArchaiusConfig {

  @Bean
  DynamicStringProperty dynamicStringProperty() {
    return DynamicPropertyFactory.getInstance()
        .getStringProperty("dsp-value", "no default value specified");
  }

}
