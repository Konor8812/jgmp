package com.illia.repository.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.illia.model.Event;
import com.illia.repository.EventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

  private final DynamoDBMapper dynamoDBMapper;

  @Override
  public Event save(Event event) {
    dynamoDBMapper.save(event);
    return event;
  }

  @Override
  public Event findById(long eventId) {
    return dynamoDBMapper.load(Event.class, eventId);
  }

  @Override
  public List<Event> findAll() {
    var res = dynamoDBMapper.scan(Event.class, new DynamoDBScanExpression());
    return res.stream().toList();
  }

  @Override
  public void deleteById(long eventId) {
    var deleteDummy = new Event();
    deleteDummy.setId(eventId);
    dynamoDBMapper.delete(deleteDummy);
  }
}
