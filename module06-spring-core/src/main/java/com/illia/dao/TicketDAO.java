package com.illia.dao;

import com.illia.data.DataStorage;
import com.illia.model.Ticket;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketDAO {

  private final Logger logger = LoggerFactory.getLogger(TicketDAO.class);
  private DataStorage dataStorage;
  private static final String NAMESPACE = "ticket:";

  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  public Ticket saveTicket(Ticket ticket) {
    logger.debug(String.format("Saving ticket with id=%s userId=%s eventId=%s place=%s category=%s",
        ticket.getId(),
        ticket.getUserId(),
        ticket.getEventId(),
        ticket.getPlace(),
        ticket.getCategory()));
    return (Ticket) dataStorage.save(NAMESPACE + ticket.getId(), ticket);
  }

  public Ticket getTicketById(long id) {
    return (Ticket) dataStorage.get(NAMESPACE + id);
  }

  public Ticket updateTicket(Ticket ticket) {
    return (Ticket) dataStorage.update(NAMESPACE + ticket.getId(), ticket);
  }

  public Ticket deleteTicket(long id) {
    return (Ticket) dataStorage.delete(NAMESPACE + id);
  }

  public List<Ticket> getAllTickets() {
    return dataStorage.getAll(NAMESPACE).stream().map(x -> (Ticket) x).collect(Collectors.toList());
  }
}
