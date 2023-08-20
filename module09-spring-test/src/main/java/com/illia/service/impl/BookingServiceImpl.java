package com.illia.service.impl;

import com.illia.model.BookTicketRequest;
import com.illia.service.BookingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

  private final JmsTemplate jmsTemplate;
  private final String queue;


  public BookingServiceImpl(JmsTemplate jmsTemplate,
      @Value("${jms.bookTicket.queue}") String queue) {
    this.jmsTemplate = jmsTemplate;
    this.queue = queue;
  }


  @Override
  public void sendBookTicketRequest(BookTicketRequest bookTicketRequest) {
    jmsTemplate.convertAndSend(queue, bookTicketRequest);
  }
}
