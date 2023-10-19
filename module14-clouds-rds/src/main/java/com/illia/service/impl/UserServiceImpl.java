package com.illia.service.impl;

import com.illia.model.User;
import com.illia.repository.UserRepository;
import com.illia.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public boolean deleteUser(long userId) {
    userRepository.deleteById(userId);
    return true;
  }

  @Override
  public User updateUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public List<User> getUsersByName(String name) {
    return userRepository.findAll().stream()
        .filter(x -> x.getName().equals(name))
        .collect(Collectors.toList());
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User getUserById(long userId) {
    return userRepository.findById(userId).get();
  }
}
