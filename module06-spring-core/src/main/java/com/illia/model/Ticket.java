package com.illia.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ticket {

  private long id;
  private long eventId;
  private long userId;
  private int place;
  private Category category;

  public enum Category {STANDARD, PREMIUM, BAR}
}
