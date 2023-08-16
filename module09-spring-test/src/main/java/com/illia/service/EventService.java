package com.illia.service;

import com.illia.model.Event;
import java.util.Date;
import java.util.List;

public interface EventService {

  boolean deleteEvent(long eventId);

  Event updateEvent(Event event);

  Event createEvent(Event event);

  List<Event> getEventsForDay(Date day);

  List<Event> getEventByTitle(String title);

  Event getEventById(long eventId);
}
