package com.illia.facade;

import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import com.illia.service.EventService;
import com.illia.service.TicketService;
import com.illia.service.UserAccountService;
import com.illia.service.UserService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BookingFacadeImpl implements BookingFacade {

  private final UserService userService;
  private final TicketService ticketService;
  private final EventService eventService;
  private final UserAccountService userAccountService;

  @Override
  public Event getEventById(long eventId) {
    return eventService.getEventById(eventId);
  }

  @Override
  public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
    return eventService.getEventByTitle(title).stream()
        .skip((long) pageNum * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
  }

  @Override
  public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
    return eventService.getEventsForDay(day).stream()
        .skip((long) pageNum * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
  }

  @Override
  public Event createEvent(Event event) {
    return eventService.createEvent(event);
  }

  @Override
  public Event updateEvent(Event event) {
    return eventService.updateEvent(event);
  }

  @Override
  public boolean deleteEvent(long eventId) {
    eventService.deleteEvent(eventId);
    return true;
  }

  @Override
  public User getUserById(long userId) {
    return userService.getUserById(userId);
  }

  @Override
  public User getUserByEmail(String email) {
    return userService.getUserByEmail(email);
  }

  @Override
  public List<User> getUsersByName(String name, int pageSize, int pageNum) {
    return userService.getUsersByName(name).stream()
        .skip((long) pageNum * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
  }

  @Override
  public User createUser(User user) {
    return userService.createUser(user);
  }

  @Override
  public User updateUser(User user) {
    return userService.updateUser(user);
  }

  @Override
  public boolean deleteUser(long userId) {
    userService.deleteUser(userId);
    return true;
  }

  @Override
  public Ticket bookTicket(long userId, long eventId, int place, Category category) {
    return ticketService.bookTicket(userId, eventId, place, category);
  }

  @Override
  public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
    return ticketService.getBookedTicketsByUser(user).stream()
        .skip((long) pageNum * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
  }

  @Override
  public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
    return ticketService.getBookedTicketsByEvent(event).stream()
        .skip((long) pageNum * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
  }

  @Override
  public boolean cancelTicket(long ticketId) {
    return ticketService.cancelTicket(ticketId);
  }

  @Override
  public void refillAccountFunds(long id, long amount) {
    userAccountService.refill(id, amount);
  }
}
