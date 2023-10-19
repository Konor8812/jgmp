package com.illia;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Order {

  private final String customerName;
  private final List<Product> positions;
  private double total;

  public Order(String customerName) {
    this.customerName = customerName;
    positions = new ArrayList<>();
  }

  public void addPosition(Product product) {
    positions.add(product);
    total += product.getAmount() * product.getPrice();
  }
}
