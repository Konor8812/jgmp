package com.illia.service;

import com.illia.model.User;
import java.util.List;

public interface UserService {

  void deleteUser(long userId);

  User updateUser(User user);

  User createUser(User user);

  List<User> getUsersByName(String name);

  User getUserByEmail(String email);

  User getUserById(long userId);
}
