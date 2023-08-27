package com.illia.task3.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements AuthenticationManager {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserDetailsService userDetailsService;


  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    var username = (String) authentication.getName();

    var user = userDetailsService.loadUserByUsername(username);
    if (user != null) {
      var password = (String) authentication.getCredentials();
      var userPassword = user.getPassword();
      if (bCryptPasswordEncoder.matches(password, userPassword)) {
        return authentication;
      }
    }

    throw new BadCredentialsException("Bad credentials");
  }
}
