package com.illia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {

  private String name;
  private double price;
  private boolean isCountable;
  private double amount;
}
