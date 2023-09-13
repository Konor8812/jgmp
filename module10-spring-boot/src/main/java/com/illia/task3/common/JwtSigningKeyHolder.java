package com.illia.task3.common;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class JwtSigningKeyHolder {

  private final byte[] encodedSecret;

  {
    var secureRandom = new SecureRandom();
    var bytes = new byte[32];
    secureRandom.nextBytes(bytes);
    encodedSecret = Base64.getEncoder().encode(bytes);
  }

  public Key getSigningKey() {
    return Keys.hmacShaKeyFor(encodedSecret);
  }
}
