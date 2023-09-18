package com.illia.task3.service;

import com.illia.task3.common.JwtSigningKeyHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtSigningKeyHolder signingKeyHolder;

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  public String createToken(Map<String, Object> claims, UserDetails user) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .signWith(signingKeyHolder.getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String createToken(UserDetails user) {
    return createToken(new HashMap<>(), user);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(signingKeyHolder.getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }


  public boolean containsValidToken(String jwtHeader) {
    if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
      var token = jwtHeader.substring(7);
      try {
        return new Date(System.currentTimeMillis())
            .before(extractAllClaims(token).getExpiration());
      } catch (JwtException ex) {
        ex.printStackTrace();
        return false;
      }
    }

    return false;
  }


}
