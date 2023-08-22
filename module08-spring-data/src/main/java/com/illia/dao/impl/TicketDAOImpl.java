package com.illia.dao.impl;

import com.illia.dao.TicketDAO;
import com.illia.data.SessionsManager;
import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.User;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketDAOImpl implements TicketDAO {

  private final Logger logger = LoggerFactory.getLogger(TicketDAOImpl.class);
  private final SessionsManager sessionsManager;
  private final Class<Ticket> type = Ticket.class;

  @Transactional
  @Override
  public <S extends Ticket> S save(S ticket) {
    logger.info("Saving ticket: " + ticket);

    var session = sessionsManager.getSession();

    var user = session.get(User.class, ticket.getUserId());
    var event = session.get(Event.class, ticket.getEventId());
    if (user.getUserAccount().getPrepaid() < event.getPrice()) {
      throw new RuntimeException("Not enough funds to book a ticket!");
    }
    ticket.setUser(user);
    ticket.setEvent(event);
    session.persist(ticket);
    ticket.getUser().getUserAccount().withdraw(ticket.getEvent().getPrice());

    return ticket;
  }

  @Override
  public Optional<Ticket> findById(Long id) {
    var session = sessionsManager.getSession();
    var event = session.get(type, id);
    return Optional.ofNullable(event);
  }

  @Override
  @Cache(region = "findAllRegion", usage = CacheConcurrencyStrategy.READ_WRITE)
  public Iterable<Ticket> findAll() {
    var session = sessionsManager.getSession();
    var query = session.createQuery("FROM " + type.getName(), type);
    query.setCacheable(true);
    query.setCacheRegion("findAllRegion");
    return query
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
  public void delete(Ticket ticket) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.remove(ticket);
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
  public Iterable<Ticket> findAllById(Iterable<Long> ids) {
    var list = new ArrayList<Ticket>();
    var session = sessionsManager.getSession();
    for (var id : ids) {
      list.add(session.get(type, id));
    }
    return list;
  }

  @Override
  public long count() {
    var session = sessionsManager.getSession();
    return session.createQuery("SELECT COUNT(*) FROM Ticket", Long.class)
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
  public void deleteAll(Iterable<? extends Ticket> entities) {
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
  public <S extends Ticket> Iterable<S> saveAll(Iterable<S> entities) {
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
    try {
      var transaction = session.beginTransaction();
      session.createQuery("DELETE FROM Ticket").getSingleResult();
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
  }

  @Override
  public List<Ticket> getTicketsByUser(User user) {
    var session = sessionsManager.getSession();
    return session.createQuery("FROM Ticket WHERE user = :user", type)
        .setParameter("user", user)
        .getResultList();
  }

  @Override
  public List<Ticket> getTicketsByEvent(Event event) {
    var session = sessionsManager.getSession();
    return session.createQuery("FROM Ticket WHERE event = :event", type)
        .setParameter("event", event)
        .getResultList();
  }
}
