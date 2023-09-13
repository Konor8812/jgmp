package com.illia.config;

import com.illia.service.BusinessComponent;
import com.netflix.servo.DefaultMonitorRegistry;
import com.netflix.servo.monitor.BasicCounter;
import com.netflix.servo.monitor.BasicGauge;
import com.netflix.servo.monitor.Counter;
import com.netflix.servo.monitor.Gauge;
import com.netflix.servo.monitor.MonitorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServoConfig {

  @Bean
  public Gauge averageValueGauge(BusinessComponent businessComponent) {
    var gauge = new BasicGauge<Double>(MonitorConfig.builder("average value gauge")
        .build(), () -> businessComponent.getValues()
        .stream()
        .mapToDouble(Number::doubleValue)
        .average()
        .getAsDouble());
    DefaultMonitorRegistry.getInstance().register(gauge);
    return gauge;
  }

  @Bean
  public Counter serviceCallsCounter() {
    var counter = new BasicCounter(MonitorConfig.builder("invocations counter").build());
    DefaultMonitorRegistry.getInstance().register(counter);
    return counter;

  }
}
