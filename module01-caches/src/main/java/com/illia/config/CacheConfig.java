package com.illia.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CacheConfig {
  @Value("${cache.max.size}")
  private Long maxSize;

  @Value("${cache.eviction.access.time}")
  private long evictionAccessTime;

  @Value("${cache.eviction.interval}")
  private long evictionInterval;
}
