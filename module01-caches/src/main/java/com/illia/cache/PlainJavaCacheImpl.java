package com.illia.cache;

import com.illia.config.CacheConfig;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

// Thread unsafe
@Slf4j
public class PlainJavaCacheImpl<K, V> implements Cache<K, V> {

  private final CacheConfig config;

  private final Consumer<K> removalListener;
  private final Map<K, V> cache = new HashMap<>();
  private final Map<K, Long> accessTimeMap = new HashMap<>();
  private final Map<K, Long> accessesMap = new HashMap<>();

  private int size = 0;
  private long evictionCount = 0;
  private final List<Short> putTimeHistory = new ArrayList<>();

  @Override
  public V get(K key) {
    var value = cache.get(key);
    accessTimeMap.put(key, System.currentTimeMillis());
    accessesMap.put(key, accessesMap.getOrDefault(key, 0L) + 1);
    return value;
  }

  @Override
  public V put(K key, V value) {
    final var before = System.currentTimeMillis();
    if (size >= config.getMaxSize()) {
      evictLeastUsed();
    }
    accessTimeMap.put(key, System.currentTimeMillis());
    accessesMap.put(key, 0L);
    ++size;
    putTimeHistory.add((short) (System.currentTimeMillis() - before));
    return cache.put(key, value);
  }

  private void evictLeastUsed() {
    var keyToDelete = accessesMap.entrySet().stream()
        .min(Comparator.comparingLong(Entry::getValue))
        .map(Entry::getKey);
    if (keyToDelete.isPresent()) {
      evict(List.of(keyToDelete.get()));
    } else {
      evict(List.of());
    }
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
        .filter(x -> currentTime - x.getValue() > config.getEvictionAccessTime())
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
      ++evictionCount;
      --size;
    }
  }

  public long getEvictionCount() {
    return evictionCount;
  }

  public double getAveragePutTime() {
    return putTimeHistory.stream().mapToInt(Short::intValue)
        .average().orElse(-1);
  }

  public long getSize() {
    return size;
  }

  public PlainJavaCacheImpl(CacheConfig config, Consumer<K> removalListener) {
    this.config = config;
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
