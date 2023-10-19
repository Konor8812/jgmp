package com.illia.dao;

import com.illia.data.DataStorage;
import com.illia.model.Event;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDAO extends GenericDAO<Event> {

  private final Logger logger = LoggerFactory.getLogger(EventDAO.class);
  private final DataStorage<Event> dataStorage;

  @Override
  public Event save(Event event) {
    logger.debug(String.format("Saving event with id=%s title=%s date=%s",
        event.getId(),
        event.getTitle(),
        event.getDate()));
    return dataStorage.save(event);
  }

  @Override
  public Event get(long id) {
    return dataStorage.get(id, Event.class);
  }

  @Override
  public Event update(Event event) {
    return dataStorage.update(event);
  }

  @Override
  public Event delete(long id) {
    return dataStorage.delete(id, Event.class);
  }

  @Override
  public List<Event> getAll() {
    return dataStorage.getAll(Event.class);
  }
}
