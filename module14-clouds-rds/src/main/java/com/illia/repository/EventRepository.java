package com.illia.repository;

import com.illia.model.Event;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository

public interface EventRepository {


  Event save(Event event);

  Event findById(long eventId);

  List<Event> findAll();

  void deleteById(long eventId);
}
