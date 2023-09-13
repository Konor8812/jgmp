package com.illia.controller;

import com.netflix.servo.monitor.Counter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServoDemoController {

  private final Counter invocationsCounter;

  public ServoDemoController(Counter invocationsCounter) {
    this.invocationsCounter = invocationsCounter;
  }


  @GetMapping("/calls")
  public Integer getServiceCallsAmount(){
    return invocationsCounter.getValue().intValue();
  }


  @GetMapping("/service")
  public String serviceCall(){
    invocationsCounter.increment();
    return "Service logic was called";
  }
}
