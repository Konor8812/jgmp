package com.illia.dao;

import com.illia.data.DataStorage;
import com.illia.model.Event;
import java.util.List;
import java.util.stream.Collectors;

public class EventDAO {

  private DataStorage dataStorage;
  private static final String NAMESPACE = "ticket:";

  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  public Event saveEvent(Event event) {
    return (Event)dataStorage.save(NAMESPACE + event.getId(), event);
  }

  public Event getEventById(long id) {
    return (Event) dataStorage.get(NAMESPACE + id);
  }

  public Event updateEvent(Event event) {
    return (Event) dataStorage.update(NAMESPACE + event.getId(), event);
  }

  public Event deleteEvent(long id) {
    return (Event) dataStorage.delete(NAMESPACE + id);
  }

  public List<Event> getAllTEvents() {
    return dataStorage.getAll(NAMESPACE).stream().map(x -> (Event) x).collect(Collectors.toList());
  }
}
