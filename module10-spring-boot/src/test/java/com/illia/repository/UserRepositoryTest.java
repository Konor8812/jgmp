package com.illia.repository;

import com.illia.task3.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql({"classpath:sql/prepare_database.sql", "classpath:sql/insert_test_data.sql"})
public class UserRepositoryTest {
  @Autowired
  UserRepository userRepository;

  @Test
  public void findByUsernameTestShouldReturnUserFromDatabase(){
    var user = userRepository.findByUsername("testUsername");
    assertEquals("testUsername", user.getUsername());
    assertEquals("testPassword", user.getPassword());
  }

}

