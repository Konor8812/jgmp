package com.illia.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "ticket")
public class Ticket {

  private long id;
  private long eventId;
  private long userId;
  private int place;
  private Category category;

  @XmlAttribute
  public long getEventId() {
    return eventId;
  }

  @XmlAttribute
  public long getUserId() {
    return userId;
  }

  @XmlAttribute
  public int getPlace() {
    return place;
  }

  @XmlAttribute
  public Category getCategory() {
    return category;
  }

  public enum Category {STANDARD, PREMIUM, BAR}
}
