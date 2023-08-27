package com.illia.task3.controller;

import com.illia.task3.model.security.dto.AuthenticationRequestDto;
import com.illia.task3.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {

  private final AuthenticationService authenticationService;

  @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> registrationRequest(
      @RequestBody AuthenticationRequestDto authenticationRequestDto) {
    return ResponseEntity.ok(
        authenticationService.processRegistrationRequest(
            authenticationRequestDto.getUsername(),
            authenticationRequestDto.getPassword()));
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginRequest(
      @RequestBody AuthenticationRequestDto authenticationRequestDto) {
    return ResponseEntity.ok(
        authenticationService.processLoginRequest(
            authenticationRequestDto.getUsername(),
            authenticationRequestDto.getPassword()));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<String> generalExceptionsHandler(Exception ex) {
    ex.printStackTrace();
    return ResponseEntity.ok(ex.getMessage());
  }

}
