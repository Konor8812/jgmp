package com.illia.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.illia.task3.config.SecurityConfig;
import com.illia.task3.controller.AuthorizationController;
import com.illia.task3.controller.SecuredController;
import com.illia.task3.filter.JwtSecurityFilter;
import com.illia.task3.model.security.User;
import com.illia.task3.service.AuthenticationService;
import com.illia.task3.service.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.ResultMatcher.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(controllers = SecuredController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class SecuredControllerTest {

  @MockBean
  JwtService jwtService;
  @MockBean
  UserDetailsService userDetailsService;

  @SpyBean
  JwtSecurityFilter jwtSecurityFilter;

  @Autowired
  MockMvc mockMvc;

  @Test
  public void securedEndpointWithoutJwtHeaderShouldRedirectToOauth2Client() throws Exception {
    mockMvc.perform(get("http://localhost:8080/jwt"))
        .andExpect(status().is3xxRedirection());

    verify(jwtService, times(1)).containsValidToken(any());
  }

  @Test
  public void securedEndpointWithJwtShouldAuthenticateByJwt() throws Exception {
    var jwtHeader = "Bearer validToken";
    when(jwtService.containsValidToken(eq(jwtHeader)))
        .thenReturn(true);
    when(userDetailsService.loadUserByUsername(any()))
        .thenReturn(mock(User.class));

    mockMvc.perform(get("http://localhost:8080/jwt")
            .header("Authorization", jwtHeader))
        .andExpect(status().is2xxSuccessful());

    verify(jwtService, times(1)).containsValidToken(eq(jwtHeader));

  }

}
