package com.illia.cache;

import com.illia.config.CacheConfig;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlainJavaCacheImpl<K, V> implements Cache<K, V> {

  private final long maxCacheSize;
  private final long accessTimeLimit;
  private final AtomicInteger size = new AtomicInteger(0);
  private final AtomicLong evictionCount = new AtomicLong(0);
  private final Consumer<K> removalListener;
  private final Map<K, V> cache = new ConcurrentHashMap<>();
  private final Map<K, Long> accessTimeMap = new ConcurrentHashMap<>();
  private final Map<K, Long> accessesMap = new ConcurrentHashMap<>();
  private final Queue<Short> putTimeHistory = new ConcurrentLinkedQueue<>();


  @Override
  public V get(K key) {
    var value = cache.get(key);
    if (value != null) {
      accessTimeMap.put(key, System.currentTimeMillis());
      accessesMap.put(key, accessesMap.getOrDefault(key, 0L) + 1);
    }
    return value;
  }

  @Override
  public V put(K key, V value) {
    final var before = System.currentTimeMillis();
    if (size.get() >= maxCacheSize) {
      evictLeastUsed();
    }
    var cachedValue = cache.put(key, value);
    accessTimeMap.put(key, System.currentTimeMillis());
    accessesMap.put(key, 0L);
    if (cachedValue == null) {
      size.incrementAndGet();
    }
    putTimeHistory.add((short) (System.currentTimeMillis() - before));
    return cachedValue;
  }

  private void evictLeastUsed() {
    accessesMap.entrySet().stream()
        .min(Comparator.comparingLong(Entry::getValue))
        .map(Entry::getKey)
        .ifPresent(k -> evict(List.of(k)));
  }

  private void evictBySize(long numberOfElementsToEvict) {
    var keysToDelete = accessTimeMap.entrySet().stream()
        .sorted(Entry.comparingByValue())
        .limit(numberOfElementsToEvict)
        .map(Entry::getKey)
        .toList();
    evict(keysToDelete);
  }

  public void evictByTime() {
    var currentTime = System.currentTimeMillis();
    var keysToDelete = accessTimeMap.entrySet().stream()
        .filter(x -> currentTime - x.getValue() > accessTimeLimit)
        .map(Entry::getKey)
        .toList();
    evict(keysToDelete);
  }

  public void evict(List<K> keys) {
    for (K key : keys) {
      cache.remove(key);
      accessTimeMap.remove(key);
      accessesMap.remove(key);

      removalListener.accept(key);
      evictionCount.incrementAndGet();
      size.decrementAndGet();
    }
  }

  public long getEvictionCount() {
    return evictionCount.get();
  }

  public double getAveragePutTime() {
    return putTimeHistory.stream().mapToInt(Short::intValue)
        .average().orElse(-1);
  }

  public long getSize() {
    return size.get();
  }

  public PlainJavaCacheImpl(CacheConfig config, Consumer<K> removalListener) {
    maxCacheSize = config.getMaxSize();
    accessTimeLimit = config.getEvictionAccessTime();
    this.removalListener = removalListener;
    new EvictionScheduler(config.getEvictionInterval()).start();

  }

  private class EvictionScheduler {

    private final long interval;

    public EvictionScheduler(long interval) {
      this.interval = interval;
    }

    public void start() {
      new Thread(() -> {
        while (true) {
          try {
            Thread.sleep(interval);
          } catch (InterruptedException ex) {
            log.error("Eviction scheduler interrupted", ex);
            Thread.currentThread().interrupt();
            break;
          }
          evictByTime();
        }
      }).start();
    }

  }
}
