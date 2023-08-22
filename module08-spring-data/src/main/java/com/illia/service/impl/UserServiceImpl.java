package com.illia.service.impl;

import com.illia.dao.UserDAO;
import com.illia.model.User;
import com.illia.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserDAO userDAO;

  @Override
  public void deleteUser(long userId) {
    userDAO.deleteById(userId);
  }

  @Override
  public User updateUser(User user) {
    return userDAO.updateUser(user);
  }

  @Override
  public User createUser(User user) {
    return userDAO.save(user);
  }

  @Override
  public List<User> getUsersByName(String name) {
    return userDAO.getUsersByName(name);
  }

  @Override
  public User getUserByEmail(String email) {
    return userDAO.getUserByEmail(email);
  }

  @Override
  public User getUserById(long userId) {
    return userDAO.findById(userId).get();
  }
}
