package com.illia.cache;

import com.illia.entry.SimpleEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CacheTest {

  @Autowired
  CacheImpl<String, SimpleEntry> cache;

  @Test
  public void test(){

    System.out.println("asd");

  }
}
