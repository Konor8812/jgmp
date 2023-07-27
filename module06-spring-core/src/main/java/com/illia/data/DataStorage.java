package com.illia.data;

import java.util.List;

public interface DataStorage {

  Object save(String key, Object value);

  Object get(String key);

  Object update(String key, Object value);

  Object delete(String key);

  List<Object> getAll(String namespace);

  void clean();
}
