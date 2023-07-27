package com.illia.data;

import java.util.List;
import java.util.Map;

public interface DataStorage {

  Object save(String key, Object value);

  Object get(String key);

  Object update(String key, Object value);

  Object delete(String key);

  List<Object> getAll(String namespace);
}
