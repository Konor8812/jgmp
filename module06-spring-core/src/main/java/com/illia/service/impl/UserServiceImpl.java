package com.illia.service.impl;

import com.illia.dao.UserDAO;
import com.illia.model.User;
import com.illia.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

  private UserDAO userDAO;

  public void setUserDAO(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @Override
  public boolean deleteUser(long userId) {
    return userDAO.deleteUser(userId) != null;
  }

  @Override
  public User updateUser(User user) {
    return userDAO.updateUser(user);
  }

  @Override
  public User createUser(User user) {
    return userDAO.saveUser(user);
  }

  @Override
  public List<User> getUsersByName(String name) {
    return userDAO.getAllUsers().stream()
        .filter(x -> x.getName().equals(name))
        .collect(Collectors.toList());
  }

  @Override
  public User getUserByEmail(String email) {
    return userDAO.findUserByEmail(email);
  }

  @Override
  public User getUserById(long userId) {
    return userDAO.getUserById(userId);
  }
}
