package com.illia.data.provider;

import com.illia.entry.SimpleEntry;


public class SimpleEntryProvider implements DataProvider<String, SimpleEntry> {

  public SimpleEntry getData(String key) {
    return new SimpleEntry("Value for " + key);

  }

}
