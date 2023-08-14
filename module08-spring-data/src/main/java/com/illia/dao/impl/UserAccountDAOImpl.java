package com.illia.dao.impl;

import com.illia.dao.UserAccountDAO;
import com.illia.data.SessionsManager;
import com.illia.model.UserAccount;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserAccountDAOImpl implements UserAccountDAO {

  private final Logger logger = LoggerFactory.getLogger(UserAccountDAOImpl.class);
  private final SessionsManager sessionsManager;
  private final Class<UserAccount> type = UserAccount.class;

  @Override
  public <S extends UserAccount> S save(S userAccount) {
    logger.info("Saving userAccount: " + userAccount);

    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.persist(userAccount);
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }

    return userAccount;
  }

  @Override
  public Optional<UserAccount> findById(Long id) {
    var session = sessionsManager.getSession();
    var event = session.get(type, id);
    return Optional.ofNullable(event);
  }

  @Override
  public Iterable<UserAccount> findAll() {
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
  public void delete(UserAccount userAccount) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.remove(userAccount);
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
  public Iterable<UserAccount> findAllById(Iterable<Long> ids) {
    var list = new ArrayList<UserAccount>();
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
  public void deleteAll(Iterable<? extends UserAccount> entities) {
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
  public <S extends UserAccount> Iterable<S> saveAll(Iterable<S> entities) {
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
      session.createQuery("DELETE FROM UserAccount").getSingleResult();
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
  }

  @Override
  public void refill(Long id, long amount) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      var account = session.get(type, id);
      account.refill(amount);
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
  }
}
