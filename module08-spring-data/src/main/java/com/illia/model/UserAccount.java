package com.illia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_account_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column
  private long prepaid;

  public void refill(long amount) {
    prepaid += amount;
  }

  public void withdraw(long amount) {
    prepaid -= amount;
  }
}
