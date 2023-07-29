package com.illia.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {

  private long id;
  private String title;
  private Date date;
}
