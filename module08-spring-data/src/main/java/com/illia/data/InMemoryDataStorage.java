package com.illia.data;

import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.User;
import com.illia.model.UserAccount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class InMemoryDataStorage<T> implements DataStorage<T> {

  private Map<String, Object> storage;

  public InMemoryDataStorage() {
    storage = new HashMap<>();
  }

  @Override
  public T save(T value) {
    var prefix = resolvePrefixByType(value.getClass());
    String key = null;
    switch (prefix) {
      case "user:" -> {
        var user = (User) value;
        key = prefix + user.getId();
      }
      case "event:" -> {
        var event = (Event) value;
        key = prefix + event.getId();
      }
      case "ticket:" -> {
        var ticket = (Ticket) value;
        key = prefix + ticket.getId();
      }
      case "userAccount" -> {
        var userAccount = (UserAccount) value;
        key = prefix + userAccount.getId();
      }
    }
    storage.put(key, value);
    return (T) storage.get(key);
  }

  @Override
  public T get(Long id, Class<?> type) {
    return (T) storage.get(resolvePrefixByType(type) + id);
  }

  @Override
  public T update(T value) {
    var prefix = resolvePrefixByType(value.getClass());
    switch (prefix) {
      case "user:" -> {
        return (T) storage.put(prefix + ((User) value).getId(), value);
      }
      case "event:" -> {
        return (T) storage.put(prefix + ((Event) value).getId(), value);
      }
      case "ticket:" -> {
        return (T) storage.put(prefix + ((Ticket) value).getId(), value);
      }
      case "userAccount" -> {
        return (T) storage.put(prefix + ((UserAccount) value).getId(), value);
      }
      default -> {
        return null;
      }
    }
  }

  @Override
  public T delete(Long id, Class<?> type) {
    return (T) storage.remove(resolvePrefixByType(type) + id);
  }

  @Override
  public List<T> getAll(Class<?> type) {
    var prefix = resolvePrefixByType(type);
    return storage.entrySet().stream()
        .filter(entry -> entry.getKey().contains(prefix))
        .map(entry -> (T) entry.getValue())
        .collect(Collectors.toList());
  }

  @Override
  public void clean() {
    storage = new HashMap<>();
  }

  private String resolvePrefixByType(Class<?> type) {
    return switch (type.getSimpleName()) {
      case "User" -> "user:";
      case "Event" -> "event:";
      case "Ticket" -> "ticket:";
      case "UserAccount" -> "userAccount:";
      default -> null;
    };
  }

  String filePath;

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public void postConstruct() {
  }
}
