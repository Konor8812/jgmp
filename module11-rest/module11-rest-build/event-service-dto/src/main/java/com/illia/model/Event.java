package com.illia.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

  private long id;
  private String title;
  private String place;
  private String speaker;
  private String eventType;
  private Date dateTime;
}
