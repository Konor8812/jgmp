package com.illia.dao.impl;

import com.illia.dao.EventDAO;
import com.illia.data.SessionsManager;
import com.illia.model.Event;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventDAOImpl implements EventDAO {

  private final Logger logger = LoggerFactory.getLogger(EventDAOImpl.class);
  private final SessionsManager sessionsManager;
  private final Class<Event> type = Event.class;

  @Override
  public <S extends Event> S save(S event) {
    logger.info(String.format("Saving event with id=%s title=%s date=%s",
        event.getId(),
        event.getTitle(),
        event.getDate()));

    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.persist(event);
      transaction.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
      session.getTransaction().rollback();
    }

    return event;
  }

  @Override
  public Optional<Event> findById(Long id) {
    var session = sessionsManager.getSession();
    var event = session.get(type, id);
    return Optional.ofNullable(event);
  }

  @Override
  public Iterable<Event> findAll() {
    var session = sessionsManager.getSession();

    return session.createQuery("FROM " + type.getName(), type)
        .getResultList();
  }

  @Override
  public void deleteById(Long id) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.remove(session.get(type, id));
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
  }

  @Override
  public void delete(Event event) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.remove(event);
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
  }

  @Override
  public boolean existsById(Long id) {
    return findById(id).isPresent();
  }

  @Override
  public Iterable<Event> findAllById(Iterable<Long> ids) {
    var list = new ArrayList<Event>();
    var session = sessionsManager.getSession();
    for (var id : ids) {
      list.add(session.get(type, id));
    }
    return list;
  }

  @Override
  public long count() {
    var session = sessionsManager.getSession();
    return session.createQuery("SELECT COUNT(*) FROM Event", Long.class)
        .getSingleResult();
  }

  @Override
  public void deleteAllById(Iterable<? extends Long> ids) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      for (var id : ids) {
        session.remove(session.get(type, id));
      }
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
  }

  @Override
  public void deleteAll(Iterable<? extends Event> entities) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      for (var entity : entities) {
        session.remove(entity);
      }
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
  }

  @Override
  public <S extends Event> Iterable<S> saveAll(Iterable<S> entities) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      for (var entity : entities) {
        session.persist(entity);
      }
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
    return entities;
  }

  @Override
  public void deleteAll() {
    var session = sessionsManager.getSession();
    session.createQuery("DELETE FROM Event ");
  }

  @Override
  public List<Event> getEventsByTitle(String title) {
    var session = sessionsManager.getSession();
    return session.createQuery("FROM Event WHERE title = :title", type)
        .setParameter("title", title)
        .getResultList();
  }

  @Override
  public List<Event> getEventsForDay(Date date) {
    var session = sessionsManager.getSession();
    return session.createQuery("FROM Event WHERE date = :date", type)
        .setParameter("date", date)
        .getResultList();
  }

  @Override
  public Event updateEvent(Event event) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.merge(event);
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
    return event;
  }
}
