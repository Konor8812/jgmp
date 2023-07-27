package com.illia.data;

import com.illia.model.Event;
import com.illia.model.Ticket;
import com.illia.model.User;
import jakarta.annotation.PostConstruct;
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
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

public class InMemoryDataStorage implements DataStorage {

  private Map<String, Object> storage;

  public InMemoryDataStorage() {
    storage = new HashMap<>();
  }

  @Override
  public Object save(String key, Object value) {
    return storage.put(key, value);
  }

  @Override
  public Object get(String key) {
    return storage.get(key);
  }

  @Override
  public Object update(String key, Object value) {
    return storage.put(key, value);
  }

  @Override
  public Object delete(String key) {
    return storage.remove(key);
  }

  @Override
  public List<Object> getAll(String namespace) {
    return storage.entrySet().stream()
        .filter(x -> x.getKey().contains(namespace))
        .map(Entry::getValue)
        .collect(Collectors.toList());
  }

  @Override
  public void clean() {
    storage = new HashMap<>();
  }

  String filePath;

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  @PostConstruct
  public void postConstruct() {
    var resource = new ClassPathResource(filePath);
    try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      String line;

      while (!((line = reader.readLine()) == null)) {
        var key = line.substring(0, line.indexOf(","));
        var values = line.substring(line.indexOf(",")).split(":");
        if (key.contains("user")) {
          storage.put(key, new User() {
            @Override
            public long getId() {
              return Long.parseLong(values[0]);
            }

            @Override
            public void setId(long id) {

            }

            @Override
            public String getName() {
              return values[2];
            }

            @Override
            public void setName(String name) {

            }

            @Override
            public String getEmail() {
              return values[3];
            }

            @Override
            public void setEmail(String email) {

            }
          });

        } else if (key.contains("event")) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
          storage.put(key, new Event() {
            @Override
            public long getId() {
              return Long.parseLong(values[0]);
            }

            @Override
            public void setId(long id) {

            }

            @Override
            public String getTitle() {
              return values[2];
            }

            @Override
            public void setTitle(String title) {

            }

            @Override
            public Date getDate() {
              return Date.from(LocalDate.parse(values[3], formatter)
                  .atStartOfDay()
                  .toInstant(ZoneOffset.UTC));
            }

            @Override
            public void setDate(Date date) {

            }
          });

        } else if (key.contains("ticket")) {
          storage.put(key, new Ticket() {
            @Override
            public long getId() {
              return Long.parseLong(values[0]);
            }

            @Override
            public void setId(long id) {

            }

            @Override
            public long getEventId() {
              return Long.parseLong(values[1]);
            }

            @Override
            public void setEventId(long eventId) {

            }

            @Override
            public long getUserId() {
              return Long.parseLong(values[2]);
            }

            @Override
            public void setUserId(long userId) {

            }

            @Override
            public Category getCategory() {
              return Category.valueOf(values[3]);
            }

            @Override
            public void setCategory(Category category) {

            }

            @Override
            public int getPlace() {
              return Integer.parseInt(values[4]);
            }

            @Override
            public void setPlace(int place) {

            }
          });
        }

      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
