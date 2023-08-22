package com.illia.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.illia.data.DataStorage;
import com.illia.model.BookTicketRequest;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.service.impl.TicketServiceImpl;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BookTicketE2ETest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  DataStorage<Ticket> dataStorage;

  @SpyBean
  TicketServiceImpl ticketService;

  @Test
  public void shouldProceedBookTicketRequestAndSendMessageToJms_ThenProceedMessage()
      throws Exception {

    var latch = new CountDownLatch(1);

    doAnswer(answer -> {
      answer.callRealMethod();
      latch.countDown();
      return null;
    }).when(ticketService).receiveBookingRequest(any(BookTicketRequest.class));

    mockMvc.perform(post("http://localhost:8080/ticket/book")
            .contentType("application/json")
            .content("{\"userId\":1, \"eventId\":1, \"place\":1, \"category\":\"BAR\" }"))
        .andExpect(status().isOk())
        .andExpect(content().string("Request sent"));

    latch.await();

    verify(ticketService, times(1)).receiveBookingRequest(any());

    var res = dataStorage.getAll(Ticket.class);
    assertEquals(1, res.size());

    var ticket = res.get(0);
    assertEquals(1, ticket.getUserId());
    assertEquals(1, ticket.getEventId());
    assertEquals(1, ticket.getPlace());
    assertEquals(Category.BAR, ticket.getCategory());
  }
}
