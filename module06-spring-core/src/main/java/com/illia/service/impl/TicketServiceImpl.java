package com.illia.service.impl;

import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.User;
import com.illia.service.TicketService;
import java.util.List;

public class TicketServiceImpl implements TicketService {

  @Override
  public boolean cancelTicket(long ticketId) {
    return false;
  }

  @Override
  public List<Ticket> getBookedTicketsByEvent(Event event) {
    return null;
  }

  @Override
  public List<Ticket> getBookedTicketsByUser(User user) {
    return null;
  }

  @Override
  public Ticket bookTicket(Ticket ticket) {
    return null;
  }
}
