package com.illia.security;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illia.task3.model.security.User;
import com.illia.task3.security.AuthenticationManagerImpl;
import com.illia.task3.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthenticationManagerTest {

  BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
  UserDetailsService userDetailsService = mock(UserDetailsServiceImpl.class);

  AuthenticationManager authenticationManager = new AuthenticationManagerImpl(bCryptPasswordEncoder, userDetailsService);

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  private static final Authentication authentication
      = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);

  @Test
  public void authenticateTestShouldBadCredentialsIfUserDoesNotExist(){
    when(userDetailsService.loadUserByUsername(anyString()))
        .thenReturn(null);

    assertThrows(BadCredentialsException.class,
        () -> authenticationManager.authenticate(authentication));

    verify(userDetailsService, times(1))
        .loadUserByUsername(anyString());
  }

  @Test
  public void authenticateTestShouldBadCredentialsIfPasswordsDoNotMatch(){
    when(userDetailsService.loadUserByUsername(anyString()))
        .thenReturn(mock(User.class));
    when(bCryptPasswordEncoder.matches(eq(PASSWORD), any()))
        .thenReturn(false);

    assertThrows(BadCredentialsException.class,
        () -> authenticationManager.authenticate(authentication));

    verify(userDetailsService, times(1))
        .loadUserByUsername(anyString());
    verify(bCryptPasswordEncoder, times(1))
        .matches(eq(PASSWORD), any());
  }

  @Test
  public void authenticateTestShouldAuthenticateIfCredentialsMatch(){
    when(userDetailsService.loadUserByUsername(anyString()))
        .thenReturn(mock(User.class));
    when(bCryptPasswordEncoder.matches(eq(PASSWORD), any()))
        .thenReturn(true);

    assertEquals(authentication, authenticationManager.authenticate(authentication));

    verify(userDetailsService, times(1))
        .loadUserByUsername(anyString());
    verify(bCryptPasswordEncoder, times(1))
        .matches(eq(PASSWORD), any());
  }


}
