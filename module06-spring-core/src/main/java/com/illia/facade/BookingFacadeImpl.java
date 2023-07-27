package com.illia.facade;

import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import com.illia.service.EventService;
import com.illia.service.TicketService;
import com.illia.service.UserService;
import com.illia.service.impl.EventServiceImpl;
import com.illia.service.impl.TicketServiceImpl;
import com.illia.service.impl.UserServiceImpl;
import java.util.Date;
import java.util.List;


public class BookingFacadeImpl implements BookingFacade{

  private final UserService userService;
  private final TicketService ticketService;
  private final EventService eventService;

  public BookingFacadeImpl(UserService userService,
      TicketService ticketService,
      EventService eventService) {
    this.userService = userService;
    this.ticketService = ticketService;
    this.eventService = eventService;
  }

  @Override
  public Event getEventById(long eventId) {
    return eventService.getEventById(eventId);
  }

  @Override
  public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
    var events = eventService.getEventByTitle(title);
    return events;
  }

  @Override
  public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
    var events = eventService.getEventsForDay(day);
    return events;
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
    return eventService.deleteEvent(eventId);
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
    var users = userService.getUsersByName(name);
    return users;
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
    return userService.deleteUser(userId);
  }

  @Override
  public Ticket bookTicket(long userId, long eventId, int place, Category category) {
    var ticket = new Ticket(){

      @Override
      public long getId() {
        return 0;
      }

      @Override
      public void setId(long id) {

      }

      @Override
      public long getEventId() {
        return 0;
      }

      @Override
      public void setEventId(long eventId) {

      }

      @Override
      public long getUserId() {
        return 0;
      }

      @Override
      public void setUserId(long userId) {

      }

      @Override
      public Category getCategory() {
        return null;
      }

      @Override
      public void setCategory(Category category) {

      }

      @Override
      public int getPlace() {
        return 0;
      }

      @Override
      public void setPlace(int place) {

      }
    };
    return ticketService.bookTicket(ticket);
  }

  @Override
  public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
    var tickets = ticketService.getBookedTicketsByUser(user);
    return tickets;
  }

  @Override
  public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
    var tickets = ticketService.getBookedTicketsByEvent(event);
    return null;
  }

  @Override
  public boolean cancelTicket(long ticketId) {
    return ticketService.cancelTicket(ticketId);
  }
}
