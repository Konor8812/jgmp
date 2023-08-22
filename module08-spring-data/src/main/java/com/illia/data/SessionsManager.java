package com.illia.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionsManager {

  private final SessionFactory sessionFactory;
  private final Session session; // potentially session pull

  public SessionsManager(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
    session = sessionFactory.openSession();
  }

  public Session getSession() {
    return session;
  }

  public Session getCurrentSession(){
    return sessionFactory.getCurrentSession();
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
