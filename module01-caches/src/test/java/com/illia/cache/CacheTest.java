package com.illia.cache;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.illia.config.CacheConfig;
import com.illia.entry.SimpleEntry;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class CacheTest {


  @Test
  public void cachePutTestShouldPutAllEntities() {
    var elementsAmount = 100000L;

    var config = new CacheConfig();
    config.setMaxSize(elementsAmount);
    mockEvictionPolicies(config);

    var cache = new PlainJavaCacheImpl<String, SimpleEntry>(config, x -> {
    });
    assertEquals(0L, cache.getSize());
    fillElements(cache, elementsAmount);

    assertEquals(elementsAmount, cache.getSize());
    for (int i = 0; i < elementsAmount; i++) {
      assertNotNull(cache.get("Key " + i));
    }

  }

  @Test
  public void putOperationShouldNotExceedLimitByEvictingElements() {
    var elementsAmount = 100000L;
    var expectedElementsEvicted = 10;
    var maxSize = elementsAmount - expectedElementsEvicted;

    var config = new CacheConfig();
    config.setMaxSize(maxSize);

    mockEvictionPolicies(config);

    var cache = new PlainJavaCacheImpl<String, SimpleEntry>(config, x -> {
    });
    assertEquals(0L, cache.getSize());
    fillElements(cache, elementsAmount);

    assertEquals(maxSize, cache.getSize());
    assertEquals(expectedElementsEvicted, cache.getEvictionCount());
  }

  @Test
  public void putOperationAverageTimeShouldBeNotNegative() {
    var elementsAmount = 1000L;

    var config = new CacheConfig();
    config.setMaxSize(elementsAmount);
    mockEvictionPolicies(config);

    var cache = new PlainJavaCacheImpl<String, SimpleEntry>(config, x -> {
    });
    fillElements(cache, elementsAmount);

    assertTrue(cache.getAveragePutTime() >= 0);
  }

  @Test
  public void getOperationShouldReturnElementIfFound() {
    var key = "ExistingKey";
    var expected = new SimpleEntry("Value");

    var config = new CacheConfig();
    config.setMaxSize(10L);
    mockEvictionPolicies(config);
    var cache = new PlainJavaCacheImpl<String, SimpleEntry>(config, x -> {
    });
    cache.put(key, expected);

    assertEquals(expected, cache.get(key));
  }

  @Test
  public void getOperationShouldReturnNullIfNoSuchElement() {
    var config = new CacheConfig();
    config.setMaxSize(1L);
    mockEvictionPolicies(config);
    var cache = new PlainJavaCacheImpl<String, SimpleEntry>(config, x -> {
    });

    assertNull(cache.get("NonExistingKey"));
  }

  @Test
  public void schedulerTestShouldEvictEntitiesByAccessTime() throws InterruptedException {
    var elementsAmount = 10L;
    var evictionByAccessTime = 100;

    var config = new CacheConfig();
    config.setMaxSize(elementsAmount);
    config.setEvictionAccessTime(evictionByAccessTime);
    config.setEvictionInterval(evictionByAccessTime / 5);
    var cache = new PlainJavaCacheImpl<String, SimpleEntry>(config, x -> {
    });

    fillElements(cache, elementsAmount);

    assertEquals(elementsAmount, cache.getSize());
    Thread.sleep(evictionByAccessTime * 2);
    assertEquals(0, cache.getSize());
    assertEquals(elementsAmount, cache.getEvictionCount());
  }

  @Test
  public void schedulerTestShouldEvictLeastAccessedEntitiesFirst() throws InterruptedException {
    var elementsAmount = 5L;

    var config = new CacheConfig();
    config.setMaxSize(elementsAmount);
    mockEvictionPolicies(config);
    var cache = new PlainJavaCacheImpl<String, SimpleEntry>(config, x -> {
    });

    for (int i = 0; i < elementsAmount; i++) {
      cache.put("Key " + i, new SimpleEntry("Value " + i));
    }

    // should stay cashed since were accessed more times
    var shouldRemainKeys = new ArrayList<String>();
    for (long i = 0; i < elementsAmount - 2; i++) {
      var key = "Key " + i;
      cache.get(key);
      cache.get(key);
      shouldRemainKeys.add(key);
    }

    var shouldBeRemovedKeys = new ArrayList<String>();
    for (long i = elementsAmount - 2; i < elementsAmount; i++) {
      var key = "Key " + i;
      cache.put(key + elementsAmount, new SimpleEntry("Value " + i + elementsAmount));
      cache.get(key + elementsAmount);
      shouldBeRemovedKeys.add(key);
    }

    assertEquals(elementsAmount, cache.getSize());
    for (var shouldRemainInCacheKey : shouldRemainKeys) {
      assertNotNull(cache.get(shouldRemainInCacheKey));
    }
    for (var shouldBeEvictedKey : shouldBeRemovedKeys) {
      assertNull(cache.get(shouldBeEvictedKey));
    }
  }

  private void fillElements(Cache<String, SimpleEntry> cache, long elementsAmount) {
    for (int i = 0; i < elementsAmount; i++) {
      cache.put("Key " + i, new SimpleEntry("Value " + i));
    }
  }


  private void mockEvictionPolicies(CacheConfig config) {
    config.setEvictionInterval(Long.MAX_VALUE);
    config.setEvictionAccessTime(Long.MAX_VALUE);
  }
}
