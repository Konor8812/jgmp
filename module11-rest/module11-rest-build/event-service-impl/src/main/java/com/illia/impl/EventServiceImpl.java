package com.illia.impl;


import com.illia.data.DataStorage;
import com.illia.model.Event;
import com.illia.service.EventService;
import java.util.List;

public class EventServiceImpl implements EventService {

  private final DataStorage<Long, Event> dataStorage;

  public EventServiceImpl(DataStorage<Long, Event> dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public Event createEvent(Event event) {
    return dataStorage.save(event.getId(), event);
  }

  @Override
  public Event updateEvent(Event event) {
    return dataStorage.save(event.getId(), event);
  }

  @Override
  public Event getEvent(long id) {
    return dataStorage.get(id);
  }

  @Override
  public Event deleteEvent(long id) {
    return dataStorage.remove(id);
  }

  @Override
  public List<Event> getAllEvents() {
    return dataStorage.finalAll();
  }

  @Override
  public List<Event> getAllEventsByTitle(String title) {
    return dataStorage.findAllBy("title", title);
  }
}
