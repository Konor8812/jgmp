package com.illia.caching;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.illia.dao.UserDAO;
import com.illia.data.SessionsManager;
import com.illia.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@Sql(scripts = "classpath:createTestDatabase.sql")
@DirtiesContext
public class HibernateCachingTest {

  @Autowired
  UserDAO userDAO;

  @Autowired
  SessionsManager sessionsManager;

  @BeforeEach
  public void prepareCache(){
    userDAO.findAll();
    sessionsManager.getSessionFactory().getCache().evictRegion("findAllRegion");
    sessionsManager.getSessionFactory().getStatistics().setStatisticsEnabled(true);
  }

  @Test
  public void cachingTestShouldReceiveCachedEntityAfterSecondCall(){
    var statistics = sessionsManager.getSessionFactory().getStatistics();

    var requestsAmount = 10;

    for(int i = 0; i < requestsAmount; i++){
      userDAO.findAll();
    }

    long queryCacheHits = statistics.getQueryCacheHitCount();
    long queryCacheMisses = statistics.getQueryCacheMissCount();
    long queryCachePuts = statistics.getQueryCachePutCount();

    System.out.println("Query Cache Hits: " + queryCacheHits);
    System.out.println("Query Cache Misses: " + queryCacheMisses);
    System.out.println("Query Cache Puts: " + queryCachePuts);

    assertEquals(1, queryCacheMisses);
    assertEquals(1, queryCachePuts);
    assertEquals(requestsAmount - 1, queryCacheHits);
  }

}
