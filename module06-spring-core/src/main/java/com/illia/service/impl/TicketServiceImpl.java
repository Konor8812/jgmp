package com.illia.service.impl;

import com.illia.dao.TicketDAO;
import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.User;
import com.illia.service.TicketService;
import java.util.List;
import java.util.stream.Collectors;

public class TicketServiceImpl implements TicketService {

  private TicketDAO ticketDAO;

  private long lastBookedTicketNumber = 0;
  public void setTicketDAO(TicketDAO ticketDAO) {
    this.ticketDAO = ticketDAO;
  }

  @Override
  public boolean cancelTicket(long ticketId) {
    return ticketDAO.deleteTicket(ticketId) != null;
  }

  @Override
  public List<Ticket> getBookedTicketsByEvent(Event event) {
    return ticketDAO.getAllTickets().stream()
        .filter(x -> x.getEventId() == event.getId())
        .collect(Collectors.toList());
  }

  @Override
  public List<Ticket> getBookedTicketsByUser(User user) {
    return ticketDAO.getAllTickets().stream()
        .filter(x -> x.getUserId() == user.getId())
        .collect(Collectors.toList());
  }

  @Override
  public Ticket bookTicket(Ticket ticket) {
    var savedTicket = ticketDAO.saveTicket(ticket);
    ++lastBookedTicketNumber;
    return savedTicket;
  }

  @Override
  public long getLastBookedTicketNumber() {
    return lastBookedTicketNumber;
  }
}
