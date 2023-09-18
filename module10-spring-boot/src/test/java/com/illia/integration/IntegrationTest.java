package com.illia.integration;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.illia.task3.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Sql({"classpath:sql/prepare_database.sql", "classpath:sql/insert_test_data.sql"})
@AutoConfigureMockMvc
public class IntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  JwtService jwtService;

  @Test
  public void loginUserTestShouldReturnValidJwtThenAccessSecuredEndpoint() throws Exception {

    var result = mockMvc.perform(post("http://localhost:8080/auth/registration")
            .header("Content-Type", "application/json")
            .content("{\"username\":\"testUser1\", \"password\":\"testPassword1\" }"))
        .andExpect(status().is2xxSuccessful())
        .andReturn();

    var jwtHeader = "Bearer " + result.getResponse().getContentAsString();

    assertTrue(jwtService.containsValidToken(jwtHeader));

    mockMvc.perform(get("http://localhost:8080/jwt")
            .header("Authorization", jwtHeader))
        .andExpect(status().is2xxSuccessful());

  }

}

