package com.illia.dao.impl;

import com.illia.dao.UserDAO;
import com.illia.data.SessionsManager;
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
public class UserDAOImpl implements UserDAO {

  private final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
  private final SessionsManager sessionsManager;
  private final Class<User> type = User.class;

  @Transactional
  @Override
  public <S extends User> S save(S user) {
    logger.info("Saving user: " + user);

    var session = sessionsManager.getSession();
    session.persist(user);
    return user;
  }

  @Override
  public Optional<User> findById(Long id) {
    var session = sessionsManager.getSession();
    var event = session.get(type, id);
    return Optional.ofNullable(event);
  }

  @Override
  @Cache(region = "findAllRegion", usage = CacheConcurrencyStrategy.READ_WRITE)
  public Iterable<User> findAll() {
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
  public void delete(User user) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.remove(user);
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
  public Iterable<User> findAllById(Iterable<Long> ids) {
    var list = new ArrayList<User>();
    var session = sessionsManager.getSession();
    for (var id : ids) {
      list.add(session.get(type, id));
    }
    return list;
  }

  @Override
  public long count() {
    var session = sessionsManager.getSession();
    return session.createQuery("SELECT COUNT(*) FROM User", Long.class)
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
  public void deleteAll(Iterable<? extends User> entities) {
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
  public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
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
      session.createQuery("DELETE FROM User").getSingleResult();
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
  }

  @Override
  public User getUserByEmail(String email) {
    var session = sessionsManager.getSession();
    return session.createQuery("FROM User WHERE email = :email", type)
        .setParameter("email", email)
        .getSingleResult();
  }

  @Override
  public List<User> getUsersByName(String name) {
    var session = sessionsManager.getSession();
    return session.createQuery("FROM User WHERE name = :name", type)
        .setParameter("name", name)
        .getResultList();
  }

  @Override
  public User updateUser(User user) {
    var session = sessionsManager.getSession();
    try {
      var transaction = session.beginTransaction();
      session.merge(user);
      transaction.commit();
    } catch (Exception ex) {
      session.getTransaction().rollback();
    }
    return user;
  }
}
