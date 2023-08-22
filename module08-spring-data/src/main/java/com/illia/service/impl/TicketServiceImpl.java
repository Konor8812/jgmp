package com.illia.service.impl;

import com.illia.dao.TicketDAO;
import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import com.illia.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

  private final TicketDAO ticketDAO;

  @Override
  public boolean cancelTicket(long ticketId) {
    ticketDAO.deleteById(ticketId);
    return true;
  }

  @Override
  public List<Ticket> getBookedTicketsByEvent(Event event) {
    return ticketDAO.getTicketsByEvent(event);
  }

  @Override
  public List<Ticket> getBookedTicketsByUser(User user) {
    return ticketDAO.getTicketsByUser(user);
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
