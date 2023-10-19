package com.illia.service.impl;

import com.illia.repository.TicketRepository;
import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import com.illia.service.TicketService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

  private final TicketRepository tickerRepository;

  @Override
  public boolean cancelTicket(long ticketId) {
    tickerRepository.deleteById(ticketId);
    return true;
  }

  @Override
  public List<Ticket> getBookedTicketsByEvent(Event event) {
    return tickerRepository.findAll().stream()
        .filter(x -> x.getEventId() == event.getId())
        .collect(Collectors.toList());
  }

  @Override
  public List<Ticket> getBookedTicketsByUser(User user) {
    return tickerRepository.findAll().stream()
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

    return tickerRepository.save(ticket);
  }
}
