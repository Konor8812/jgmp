package com.illia.controller;

import com.netflix.config.DynamicStringProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {


  private final DynamicStringProperty dynamicStringProperty;

  public DemoController(DynamicStringProperty dynamicStringProperty) {
    this.dynamicStringProperty = dynamicStringProperty;
  }

  /**
   * This method retrieves the current runtime value of a
   * {@link com.netflix.config.DynamicStringProperty} which is stored as Spring bean. Dynamic
   * properties can be updated at runtime, and this method provides the most recent
   */

  @GetMapping("/value")
  public String getCurrentValue() {
    return dynamicStringProperty.getValue();
  }

}
