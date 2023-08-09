package com.illia.data;

import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

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
      default -> null;
    };
  }

  String filePath;

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public void postConstruct() {
    var resource = new ClassPathResource(filePath);
    try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      String line;

      while (!((line = reader.readLine()) == null)) {
        var key = line.substring(0, line.indexOf(","));
        var values = line.substring(line.indexOf(",") + 1).split(":");
        if (key.contains("user")) {
          storage.put(key,
              User.builder()
                  .id(Long.parseLong(values[0]))
                  .name(values[1])
                  .email(values[2])
                  .build());
        } else if (key.contains("event")) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
          storage.put(key, Event.builder()
              .id(Long.parseLong(values[0]))
              .title(values[1])
              .date(Date.from(LocalDate.parse(values[2], formatter)
                  .atStartOfDay()
                  .toInstant(ZoneOffset.UTC)))
              .build());
        } else if (key.contains("ticket")) {
          storage.put(key, Ticket.builder()
              .id(Long.parseLong(values[0]))
              .eventId(Long.parseLong(values[1]))
              .userId(Long.parseLong(values[2]))
              .category(Category.valueOf(values[3]))
              .place(Integer.parseInt(values[4]))
              .build());
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
