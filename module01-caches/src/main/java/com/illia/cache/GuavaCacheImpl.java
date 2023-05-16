package com.illia.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.illia.config.CacheConfig;
import com.illia.data.provider.DataProvider;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class GuavaCacheImpl<K, V> implements Cache<K, V> {
  private final LoadingCache<K, V> loadingCache;

  @Override
  public V get(K k) {
    return loadingCache.getUnchecked(k);
  }

  @Override
  public V put(K k, V v) {
    loadingCache.put(k, v);
    return loadingCache.getUnchecked(k);
  }

  public long getSize(){
    return loadingCache.size();
  }


  public double getAverageLoadPenalty() {
    return loadingCache.stats().averageLoadPenalty();
  }

  public long getEvictionsAmount() {
    return loadingCache.stats().evictionCount();
  }

  public GuavaCacheImpl(CacheConfig config, DataProvider<K, V> dataProvider,
      Consumer<K> onEvictAction) {

    var cacheLoader = new CacheLoader<K, V>() {
      @Override
      public V load(K key) {
        return dataProvider.getData(key);
      }
    };
    var removalListener = new RemovalListener<K, V>() {
      @Override
      public void onRemoval(RemovalNotification<K, V> notification) {
        if (notification.wasEvicted()) {
          onEvictAction.accept(notification.getKey());
        }
      }
    };
    loadingCache = CacheBuilder.newBuilder()
        .maximumSize(config.getMaxSize())
        .expireAfterAccess(config.getEvictionAccessTime(), TimeUnit.MILLISECONDS)
        .recordStats()
        .removalListener(removalListener)
        .build(cacheLoader);
  }
}
