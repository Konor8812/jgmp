package com.illia.dao;

import com.illia.model.Event;
import java.util.Date;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface EventDAO extends CrudRepository<Event, Long> {

  List<Event> getEventsByTitle(String title);

  List<Event> getEventsForDay(Date date);

  Event updateEvent(Event event);
}
