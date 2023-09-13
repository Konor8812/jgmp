package com.illia.task3.config;

import com.illia.task3.filter.JwtSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, JwtSecurityFilter jwtSecurityFilter)
      throws Exception {

    http.authorizeHttpRequests((authorizeHttpRequests) -> {
          authorizeHttpRequests
              .requestMatchers("/jwt").authenticated()
              .requestMatchers("/oauth2").authenticated()
              .anyRequest().permitAll();
        })
        .oauth2Login(Customizer.withDefaults())
        .csrf((csrf) -> csrf.disable())
        .addFilterBefore(jwtSecurityFilter, OAuth2AuthorizationRequestRedirectFilter.class);

    return http.build();
  }

  @Bean
  BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
