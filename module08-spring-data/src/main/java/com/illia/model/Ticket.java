package com.illia.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class  Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "userId", insertable = false, updatable = false)
  private long userId;
  @Column(name = "eventId", insertable = false, updatable = false)
  private long eventId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "eventId")
  private Event event;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "userId")
  private User user;

  @Column
  private int place;

  @Column
  private Category category;

  public enum Category {STANDARD, PREMIUM, BAR}
}
