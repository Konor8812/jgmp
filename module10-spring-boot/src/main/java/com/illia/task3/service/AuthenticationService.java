package com.illia.task3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  public String processRegistrationRequest(String username, String password) {
    var savedUser = userService.saveNewUser(username, password);
    return jwtService.createToken(savedUser);
  }

  public String processLoginRequest(String username, String password) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password));

    return jwtService.createToken(userService.getUserByUsername(username));
  }

}
