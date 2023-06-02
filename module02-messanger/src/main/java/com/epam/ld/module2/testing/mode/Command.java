package com.epam.ld.module2.testing.mode;

import java.util.HashMap;

public interface Command {

  void execute();

  HashMap<String, String> getKnownPlaceholders();
}
