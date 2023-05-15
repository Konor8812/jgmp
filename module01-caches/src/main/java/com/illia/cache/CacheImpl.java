package com.illia.cache;

import com.illia.config.CacheConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CacheImpl<K, V> implements Cache<K, V>{

  private final CacheConfig config;

  @Override
  public V get(K key) {
    return null;
  }

  @Override
  public V put(K key, V value) {
    return null;
  }
}
