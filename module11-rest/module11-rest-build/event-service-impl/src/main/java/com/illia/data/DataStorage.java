package com.illia.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class DataStorage<K, V> {

  private Map<K, V> storage;

  public V get(K key) {
    return storage.get(key);
  }

  public V save(K key, V value) {
    return storage.put(key, value);
  }

  public List<V> finalAll() {
    return new ArrayList<>(storage.values());
  }

  @SneakyThrows
  public List<V> findAllBy(String attrName, String attrValue) {
    var allElements = storage.values();

    if (allElements.size() > 0) {
      var type = allElements
          .stream()
          .findFirst()
          .getClass();

      var field = type.getField(attrName);
      return storage.values()
          .stream()
          .filter((element) -> {
            try {
              return field.get(element).equals(attrValue);
            } catch (IllegalAccessException e) {
              throw new RuntimeException(e);
            }
          })
          .collect(Collectors.toList());
    }
    return List.of();
  }

  public DataStorage() {
    this(new HashMap<>());
  }

  public DataStorage(Map<K, V> storage) {
    this.storage = storage;
  }

  public V remove(K id) {
    return storage.remove(id);
  }
}
