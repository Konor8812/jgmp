package com.illia.extramile;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MetricsConfig {


  @Bean
  CountedAspect countedAspect(MeterRegistry registry) {
    return new CountedAspect(registry);
  }
}
