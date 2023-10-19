package com.illia.dao;

import com.illia.data.DataStorage;
import com.illia.model.Ticket;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketDAO extends GenericDAO<Ticket> {

  private final Logger logger = LoggerFactory.getLogger(TicketDAO.class);
  private final DataStorage<Ticket> dataStorage;
  private long lastSavedTicketId = 0;

  @Override
  public Ticket save(Ticket ticket) {
    ticket.setId(++lastSavedTicketId);
    logger.debug(String.format("Saving ticket with id=%s userId=%s eventId=%s place=%s category=%s",
        ticket.getId(),
        ticket.getUserId(),
        ticket.getEventId(),
        ticket.getPlace(),
        ticket.getCategory()));
    return dataStorage.save(ticket);
  }

  @Override
  public Ticket get(long id) {
    return dataStorage.get(id, Ticket.class);
  }

  @Override
  public Ticket update(Ticket ticket) {
    return dataStorage.update(ticket);
  }

  @Override
  public Ticket delete(long id) {
    return dataStorage.delete(id, Ticket.class);
  }

  @Override
  public List<Ticket> getAll() {
    return dataStorage.getAll(Ticket.class);
  }
}
