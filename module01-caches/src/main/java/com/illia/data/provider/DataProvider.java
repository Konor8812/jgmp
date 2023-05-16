package com.illia.data.provider;

public interface DataProvider<K, V> {

  public V getData(K key);

}
