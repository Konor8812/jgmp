package com.illia;

import java.util.List;

public record Order(String customerName, List<Product> positions, double total) {

}
