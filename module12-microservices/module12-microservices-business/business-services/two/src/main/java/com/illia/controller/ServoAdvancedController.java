package com.illia.controller;

import com.netflix.servo.DefaultMonitorRegistry;
import com.netflix.servo.monitor.Counter;
import com.netflix.servo.monitor.Monitor;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServoAdvancedController {

  private final Counter invocationsCounter;

  public ServoAdvancedController(Counter invocationsCounter) {
    this.invocationsCounter = invocationsCounter;
  }

  @GetMapping("/service")
  public String serviceCall(){
    invocationsCounter.increment();
    return "Service logic was called";
  }

  @GetMapping("/stats")
  public String getStats(){
    return DefaultMonitorRegistry.getInstance()
        .getRegisteredMonitors()
        .stream()
        .map(x -> x.getConfig().getName() + " monitor's value is " + x.getValue())
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
