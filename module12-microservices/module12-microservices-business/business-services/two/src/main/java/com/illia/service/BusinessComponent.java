package com.illia.service;

import com.netflix.servo.monitor.Counter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BusinessComponent {

  private final List<Number> values  = new ArrayList<>(List.of(14, 7, 2, 12, -5, 0));
  private final Counter counter;

  public BusinessComponent(Counter counter) {
    this.counter = counter;
  }

  public List<? extends Number> getValues(){
    values.set(5, counter.getValue());
    return values;
  }
}
