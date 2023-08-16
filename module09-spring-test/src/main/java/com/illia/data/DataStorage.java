package com.illia.data;

import java.util.List;

public interface DataStorage<T> {

  T save(T value);

  T get(Long id, Class<?> type);

  T update(T value);

  T delete(Long id, Class<?> type);

  List<T> getAll(Class<?> type);

  void clean();
}
