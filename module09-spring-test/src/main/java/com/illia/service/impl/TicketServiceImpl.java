package com.illia.service.impl;

import com.illia.dao.TicketDAO;
import com.illia.model.BookTicketRequest;
import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import com.illia.service.TicketService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

  private final TicketDAO ticketDAO;

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

  @Deprecated
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

  @JmsListener(destination = "${jms.bookTicket.queue}")
  public void receiveBookingRequest(BookTicketRequest bookTicketRequest) {
    try {
      Thread.sleep(100);  // processing delay imitation
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    ticketDAO.save(Ticket.builder()
        .userId(bookTicketRequest.getUserId())
        .eventId(bookTicketRequest.getEventId())
        .place(bookTicketRequest.getPlace())
        .category(bookTicketRequest.getCategory())
        .build());
  }
}
