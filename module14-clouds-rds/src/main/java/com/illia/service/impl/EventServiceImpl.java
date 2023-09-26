package com.illia.service.impl;

import com.illia.repository.EventRepository;
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

  private final EventRepository eventRepository;


  @Override
  public boolean deleteEvent(long eventId) {
    eventRepository.deleteById(eventId);
    return  true;
  }

  @Override
  public Event updateEvent(Event event) {
    return eventRepository.save(event);
  }

  @Override
  public Event createEvent(Event event) {
    return eventRepository.save(event);
  }

  @Override
  public List<Event> getEventsForDay(Date day) {
    return eventRepository.findAll().stream()
        .filter(x -> x.getDate().equals(day))
        .collect(Collectors.toList());
  }

  @Override
  public List<Event> getEventByTitle(String title) {
    return eventRepository.findAll().stream()
        .filter(x -> x.getTitle().equals(title))
        .collect(Collectors.toList());
  }

  @Override
  public Event getEventById(long eventId) {
    return eventRepository.findById(eventId).get();
  }
}
