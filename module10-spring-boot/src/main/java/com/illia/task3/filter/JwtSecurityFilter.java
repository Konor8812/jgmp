package com.illia.task3.filter;

import com.illia.task3.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    var jwtHeader = request.getHeader("Authorization");
    if (jwtService.containsValidToken(jwtHeader)) {
      var token = jwtHeader.substring(7);
      var username = jwtService.extractUsername(token);
      var user = userDetailsService.loadUserByUsername(username);
      var authentication = createAuthentication(user);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);

  }

  private Authentication createAuthentication(UserDetails user) {
    if (user != null) {
      return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    } else {
      throw new AuthenticationServiceException("No such user / invalid jwt");
    }
  }
}
