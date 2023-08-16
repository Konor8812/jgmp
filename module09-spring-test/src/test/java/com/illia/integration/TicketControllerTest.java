package com.illia.integration;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.illia.controller.TicketController;
import com.illia.facade.BookingFacade;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TicketController.class)
@AutoConfigureMockMvc
public class TicketControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  BookingFacade bookingFacade;

  @Test
  public void testPreloadFromMultipartFile() throws Exception {
    var mockMultipart = new MockMultipartFile("dataBatch", "content".getBytes());

    mockMvc.perform(multipart("http://localhost:8080/ticket/preload")
            .file(mockMultipart))
        .andExpect(status().is3xxRedirection());

    verify(bookingFacade, times(1))
        .preloadTicketsFromInputStream(any(InputStream.class));

  }

}
