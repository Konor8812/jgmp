package com.illia.service.impl;

import com.illia.dao.EventDAO;
import com.illia.data.DataStorage;
import com.illia.model.Event;
import com.illia.service.EventService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventServiceImpl implements EventService {

private EventDAO eventDAO;

  public void setEventDAO(EventDAO eventDAO) {
    this.eventDAO = eventDAO;
  }

  @Override
  public boolean deleteEvent(long eventId) {
    return eventDAO.deleteEvent(eventId) != null;
  }

  @Override
  public Event updateEvent(Event event) {
    return eventDAO.updateEvent(event);
  }

  @Override
  public Event createEvent(Event event) {
    return eventDAO.saveEvent(event);
  }

  @Override
  public List<Event> getEventsForDay(Date day) {
    return eventDAO.getAllTEvents().stream()
        .filter(x -> x.getDate().equals(day))
        .collect(Collectors.toList());
  }

  @Override
  public List<Event> getEventByTitle(String title) {
    return eventDAO.getAllTEvents().stream()
        .filter(x -> x.getTitle().equals(title))
        .collect(Collectors.toList());
  }

  @Override
  public Event getEventById(long eventId) {
    return eventDAO.getEventById(eventId);
  }
}
