package com.illia.cache.entity;

import java.util.concurrent.atomic.AtomicLong;
import lombok.Builder;

@Builder
public class CachedValue<V> {

  private final V value;
  private final AtomicLong accessesAmount = new AtomicLong(0);
  private final AtomicLong lastAccessTime = new AtomicLong(0);

  public long incrementAndGetAccessesAmount() {
    return accessesAmount.incrementAndGet();
  }

  public void renewLastAccessTime() {
    this.lastAccessTime.set(System.currentTimeMillis());
  }

  public long getIdleTime() {
    return System.currentTimeMillis() - lastAccessTime.get();
  }

  public V getValue() {
    return value;
  }

  public CachedValue(V value) {
    this.value = value;
  }
}
