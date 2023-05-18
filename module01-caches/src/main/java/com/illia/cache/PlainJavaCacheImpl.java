package com.illia.cache;

import com.illia.cache.entity.CachedValue;
import com.illia.config.CacheConfig;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class PlainJavaCacheImpl<K, V> implements Cache<K, V> {

  private final long maxCacheSize;
  private final long accessTimeLimit;
  private final AtomicInteger size = new AtomicInteger(0);
  private final AtomicLong evictionCount = new AtomicLong(0);
  private final Consumer<K> removalListener;
  private final Map<K, CachedValue<V>> cache = new ConcurrentHashMap<>();
  private final Map<Long, Deque<K>> accessesAmountMap = new ConcurrentHashMap<>();
  private final Queue<Short> putTimeHistory = new ConcurrentLinkedDeque<>();

  private final ReentrantLock lock = new ReentrantLock();

  @Override
  public V get(K key) {
    lock.lock();
    var value = cache.get(key);
    lock.unlock();
    if (value != null) {
      lock.lock();
      value.incrementAndGetAccessesAmount();
      value.renewLastAccessTime();
      lock.unlock();
      return value.getValue();
    }
    return null;
  }

  @Override
  public V put(K key, V value) {
    if (value == null) {
      return null;
    }

    final var before = System.currentTimeMillis();
    lock.lock();
    if (size.get() >= maxCacheSize && !cache.containsKey(key)) {
      evictLeastUsed();
    }
    var cachedValue = cache.put(key, new CachedValue<>(value));
    var accessesAmount = 0L;
    if (cachedValue == null) {
      accessesAmount = 1;
      size.incrementAndGet();
    } else {
      accessesAmount = cachedValue.incrementAndGetAccessesAmount();
    }
    var correnpondingAccessAmountDeque = accessesAmountMap.get(accessesAmount);
    correnpondingAccessAmountDeque.addFirst(key);
    accessesAmountMap.put(accessesAmount, correnpondingAccessAmountDeque);
    lock.unlock();

    putTimeHistory.add((short) (System.currentTimeMillis() - before));
    return value;
  }

  private void evictLeastUsed() {
    var oldest = accessesAmountMap.get(1L).removeLast();
    evict(List.of(oldest));
  }

  public void evictByTime() {
    lock.lock();
    try {
      var keysToDelete = cache.entrySet().stream()
          .filter(x -> x.getValue().getIdleTime() > accessTimeLimit)
          .map(Entry::getKey)
          .toList();
      evict(keysToDelete);
    } finally {
      lock.unlock();
    }
  }

  public void evict(List<K> keys) {
    for (K key : keys) {
      cache.remove(key);
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

    accessesAmountMap.put(1L, new ConcurrentLinkedDeque<>());
    new EvictionScheduler(config.getEvictionInterval()).start();
  }

  private class EvictionScheduler {

    private final long interval;

    public EvictionScheduler(long interval) {
      this.interval = interval;
    }

    public void start() {
      var singleExecutor = Executors.newSingleThreadExecutor();
      singleExecutor.submit(() -> {
        while (true) {
          try {
            Thread.sleep(interval);
          } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
            break;
          }
          evictByTime();
        }
      });
    }

  }
}
