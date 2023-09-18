package com.illia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DemoController {


  @GetMapping("/")
  public ResponseEntity<String> find(
      @RequestHeader(name = "WasFilteredByCustomZuulFilter") String header) {
    assert Boolean.parseBoolean(header);
    return ResponseEntity.ok("/ endpoint called");
  }

  @GetMapping("/users")
  public ResponseEntity<String> findById(
      @RequestHeader(name = "WasFilteredByCustomZuulFilter") String header) {
    assert Boolean.parseBoolean(header);
    return ResponseEntity.ok("/users endpoint called");
  }
}
