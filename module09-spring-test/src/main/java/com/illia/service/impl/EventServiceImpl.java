package com.illia.service.impl;

import com.illia.dao.EventDAO;
import com.illia.model.Event;
import com.illia.service.EventService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

  private final EventDAO eventDAO;


  @Override
  public boolean deleteEvent(long eventId) {
    return eventDAO.delete(eventId) != null;
  }

  @Override
  public Event updateEvent(Event event) {
    return eventDAO.update(event);
  }

  @Override
  public Event createEvent(Event event) {
    return eventDAO.save(event);
  }

  @Override
  public List<Event> getEventsForDay(Date day) {
    return eventDAO.getAll().stream()
        .filter(x -> x.getDate().equals(day))
        .collect(Collectors.toList());
  }

  @Override
  public List<Event> getEventByTitle(String title) {
    return eventDAO.getAll().stream()
        .filter(x -> x.getTitle().equals(title))
        .collect(Collectors.toList());
  }

  @Override
  public Event getEventById(long eventId) {
    return eventDAO.get(eventId);
  }
}
