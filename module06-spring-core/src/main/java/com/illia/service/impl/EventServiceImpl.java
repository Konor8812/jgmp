package com.illia.service.impl;

import com.illia.model.Event;
import com.illia.service.EventService;
import java.util.Date;
import java.util.List;

public class EventServiceImpl implements EventService {


  @Override
  public boolean deleteEvent(long eventId) {
    return false;
  }

  @Override
  public Event updateEvent(Event event) {
    return null;
  }

  @Override
  public Event createEvent(Event event) {
    return null;
  }

  @Override
  public List<Event> getEventsForDay(Date day) {
    return null;
  }

  @Override
  public List<Event> getEventByTitle(String title) {
    return null;
  }

  @Override
  public Event getEventById(long eventId) {
    return null;
  }
}
