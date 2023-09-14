package com.illia.config;

import com.illia.data.DataStorage;
import com.illia.impl.EventServiceImpl;
import com.illia.model.Event;
import com.illia.service.EventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public EventService eventService(DataStorage<Long, Event> dataStorage) {
    return new EventServiceImpl(dataStorage);
  }


  @Bean
  public DataStorage dataStorage() {
    return new DataStorage();
  }

}
