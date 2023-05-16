package com.illia.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.illia.config.CacheConfig;
import com.illia.data.provider.DataProvider;
import com.illia.data.provider.SimpleEntryProvider;
import com.illia.entry.SimpleEntry;
import org.junit.jupiter.api.Test;

public class GuavaCacheImplTest {

  static DataProvider<String, SimpleEntry> dataProvider = new SimpleEntryProvider();

  @Test
  public void testCachePutOperationShouldCacheValue(){
    var config = new CacheConfig();
    mockConfig(config);

    var cache = new GuavaCacheImpl<>(config, dataProvider, (x) ->{});
    assertEquals(0, cache.getSize());

    var key = "key";
    var valueToCache = dataProvider.getData(key);
    cache.put(key, valueToCache);
    assertEquals(1, cache.getSize());
    assertEquals(cache.get(key), valueToCache);
  }

  @Test
  public void testCachePutOperationShouldOverwriteValueForKey(){
    var config = new CacheConfig();
    mockConfig(config);

    var cache = new GuavaCacheImpl<>(config, dataProvider, (x) ->{});
    assertEquals(0, cache.getSize());

    var key = "key";
    var shouldBeOverwritten = dataProvider.getData(key + " 1");
    var shouldOverwriteExistingEntry = dataProvider.getData(key + " 2");
    cache.put(key, shouldBeOverwritten);
    cache.put(key, shouldOverwriteExistingEntry);

    assertEquals(1, cache.getSize());
    assertEquals(cache.get(key), shouldOverwriteExistingEntry);
  }


  @Test
  public void testCacheSizeShouldNotBeExceeded(){
    var cacheSize = 10L;
    var config = new CacheConfig();
    config.setMaxSize(cacheSize);
    config.setEvictionAccessTime(Long.MAX_VALUE);

    var cache = new GuavaCacheImpl<>(config, dataProvider, (x) ->{});
    assertEquals(0, cache.getSize());

    for(int i = 0; i < cacheSize + 2; i++) {
      cache.put("key " + i, dataProvider.getData("key " + i));
    }
    assertEquals(cacheSize, cache.getSize());
  }

  @Test
  public void testGetOperationShouldReturnValueIfStoredOtherwiseLoad(){
    var config = new CacheConfig();
    mockConfig(config);

    var cache = new GuavaCacheImpl<>(config, dataProvider, (x) ->{});
    assertEquals(0, cache.getSize());

    assertNotNull(cache.get("key"));
    assertEquals(1, cache.getSize());
    assertNotNull(cache.get("key"));
    assertEquals(1, cache.getSize());
  }

  @Test
  public void testAverageLoadPenaltyShouldBePositive(){
    var cacheSize = 100L;
    var config = new CacheConfig();
    config.setMaxSize(cacheSize);
    config.setEvictionAccessTime(Long.MAX_VALUE);

    var cache = new GuavaCacheImpl<>(config, dataProvider, (x) ->{});

    for(int i = 0; i < cacheSize; i++){
      cache.get("key " + i);
    }
    assertTrue(cache.getAverageLoadPenalty() > 0); // '>=' might be better
  }

  @Test
  public void testValuesShouldBeEvictedIfExceedEvictionAccessTime() throws InterruptedException {
    var cacheSize = 10L;
    var evictionTime = 10L;
    var config = new CacheConfig();
    config.setMaxSize(cacheSize);
    config.setEvictionAccessTime(evictionTime);

    var cache = new GuavaCacheImpl<>(config, dataProvider, (x) ->{});

    for(int i = 0; i < cacheSize; i++){
      cache.put("key " + i, dataProvider.getData("key " + i));
    }
    assertEquals(cacheSize, cache.getSize());

    Thread.sleep(50);
    cache.get("not cached key"); // simulates cache activity required for eviction start
    assertEquals(1, cache.getSize());
  }

  @Test
  public void testGetEvictionsCountShouldCountAllEvictions(){
    var cacheSize = 10L;
    var config = new CacheConfig();
    config.setMaxSize(cacheSize);
    config.setEvictionAccessTime(Long.MAX_VALUE);

    var cache = new GuavaCacheImpl<>(config, dataProvider, (x) ->{});

    for(int i = 0; i < cacheSize * 2; i++){
      cache.put("key " + i, dataProvider.getData("key"));
    }

    assertEquals(cacheSize, cache.getEvictionsAmount());
  }

  private void mockConfig(CacheConfig cacheConfig){
    cacheConfig.setMaxSize(Long.MAX_VALUE);
    cacheConfig.setEvictionAccessTime(Long.MAX_VALUE);
    cacheConfig.setEvictionInterval(Long.MAX_VALUE);
  }

}
