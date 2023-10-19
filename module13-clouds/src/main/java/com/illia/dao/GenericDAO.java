package com.illia.dao;

import java.util.List;

public abstract class GenericDAO<T> {

  public abstract T save(T entity);

  public abstract T get(long id);

  public abstract T update(T entity);

  public abstract T delete(long id);

  public abstract List<T> getAll();

}
