package com.illia.service;

import com.illia.model.BookTicketRequest;
import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import java.util.List;

public interface TicketService {

  boolean cancelTicket(long ticketId);

  List<Ticket> getBookedTicketsByEvent(Event event);

  List<Ticket> getBookedTicketsByUser(User user);

  Ticket bookTicket(long userId, long eventId, int place, Category category);
}
