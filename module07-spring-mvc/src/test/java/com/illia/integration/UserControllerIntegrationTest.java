package com.illia.integration;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.illia.facade.BookingFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  BookingFacade bookingFacade;


  @Test
  public void createUserAndSaveToStorage() throws Exception {
    var res = mockMvc.perform(post("http://localhost:8080/user")
            .param("name", "testUsername")
            .param("email", "testUserEmail")
            .param("id", "11"))
        .andExpect(status().isOk())
        .andReturn()
        .getModelAndView();

    assertEquals("user", res.getViewName());
    assertNotNull(res.getModel().get("entity"));
    assertEquals("testUsername", bookingFacade.getUserById(11).getName());
  }

}
