package com.illia.config;

import lombok.Data;

@Data
public class CacheConfig {

  private Long maxSize;

  private long evictionAccessTime;

  private long evictionInterval;
}
