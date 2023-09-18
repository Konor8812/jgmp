package com.illia.task3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SecuredController {

  @GetMapping("/jwt")
  public ResponseEntity<String> getFromJwtAuth() {
    return ResponseEntity.ok("Jwt passed");
  }

  @GetMapping("/oauth2")
  public ResponseEntity<String> getFromOAuth2() {
    return ResponseEntity.ok("Oauth2 passed");
  }


  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<String> generalExceptionsHandler(Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.ok(ex.getMessage());
  }
}
