package com.illia.service;

import com.illia.model.Event;
import java.util.List;

public interface EventService {

  public Event createEvent(Event event);

  public Event updateEvent(Event event);

  public Event getEvent(long id);

  public Event deleteEvent(long id);

  public List<Event> getAllEvents();

  public List<Event> getAllEventsByTitle(String title);
}
