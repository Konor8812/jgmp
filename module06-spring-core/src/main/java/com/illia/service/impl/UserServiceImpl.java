package com.illia.service.impl;

import com.illia.model.User;
import com.illia.service.UserService;
import java.util.List;

public class UserServiceImpl implements UserService {

  @Override
  public boolean deleteUser(long userId) {
    return false;
  }

  @Override
  public User updateUser(User user) {
    return null;
  }

  @Override
  public User createUser(User user) {
    return null;
  }

  @Override
  public List<User> getUsersByName(String name) {
    return null;
  }

  @Override
  public User getUserByEmail(String email) {
    return null;
  }

  @Override
  public User getUserById(long userId) {
    return null;
  }
}
