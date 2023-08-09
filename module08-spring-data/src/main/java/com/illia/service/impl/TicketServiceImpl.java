package com.illia.service.impl;

import com.illia.dao.TicketDAO;
import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import com.illia.service.TicketService;
import java.util.List;
import java.util.stream.Collectors;

public class TicketServiceImpl implements TicketService {

  private TicketDAO ticketDAO;

  public void setTicketDAO(TicketDAO ticketDAO) {
    this.ticketDAO = ticketDAO;
  }

  @Override
  public boolean cancelTicket(long ticketId) {
    return ticketDAO.delete(ticketId) != null;
  }

  @Override
  public List<Ticket> getBookedTicketsByEvent(Event event) {
    return ticketDAO.getAll().stream()
        .filter(x -> x.getEventId() == event.getId())
        .collect(Collectors.toList());
  }

  @Override
  public List<Ticket> getBookedTicketsByUser(User user) {
    return ticketDAO.getAll().stream()
        .filter(x -> x.getUserId() == user.getId())
        .collect(Collectors.toList());
  }

  @Override
  public Ticket bookTicket(long userId, long eventId, int place, Category category) {
    var ticket = Ticket.builder()
        .userId(userId)
        .eventId(eventId)
        .place(place)
        .category(category)
        .build();

    return ticketDAO.save(ticket);
  }
}
