package com.illia.dao;

import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TicketDAO extends CrudRepository<Ticket, Long> {

  List<Ticket> getTicketsByUser(User user);

  List<Ticket> getTicketsByEvent(Event event);
}
