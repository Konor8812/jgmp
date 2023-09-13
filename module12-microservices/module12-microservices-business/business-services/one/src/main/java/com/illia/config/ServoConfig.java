package com.illia.config;

import com.netflix.servo.monitor.BasicCounter;
import com.netflix.servo.monitor.Counter;
import com.netflix.servo.monitor.MonitorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServoConfig {

  @Bean
  public Counter simpleCounter(){
    return new BasicCounter(MonitorConfig.builder("invocations counter").build());
  }
}
