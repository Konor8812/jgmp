package com.illia.dao;

import com.illia.data.DataStorage;
import com.illia.model.Ticket;
import java.util.List;
import java.util.stream.Collectors;

public class TicketDAO {

  private DataStorage dataStorage;
  private static final String NAMESPACE = "ticket:";

  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }
  public Ticket saveTicket(Ticket ticket) {
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
