package com.illia;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Product(String name, double price, boolean isCountable, double amount) {


}
