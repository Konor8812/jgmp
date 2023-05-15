package com.illia.cache;

public interface Cache<K, V> {

  public V get(K k);

  public V put(K k, V v);
}
