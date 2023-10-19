package com.illia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

  private long id;
  private long eventId;
  private long userId;
  private int place;
  private Category category;

  public enum Category {STANDARD, PREMIUM, BAR}
}
