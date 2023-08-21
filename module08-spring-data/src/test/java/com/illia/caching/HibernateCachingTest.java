package com.illia.caching;

import com.illia.dao.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class HibernateCachingTest {


  @Autowired
  UserDAO userDAO;

  @Test
  public void cachingTestShouldReceiveCachedEntityAfterSecondCall(){

  }

}
