package com.illia.nosql.service;

import com.illia.nosql.entity.Sport;
import com.illia.nosql.entity.User;
import com.illia.nosql.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User createUser(final User user) {
    return userRepository.save(user);
  }

  public User findUserById(final UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("No such user!"));
  }

  public User findUserByEmail(final String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("No such user!"));
  }

  public User findUserBySportName(final String sportName) {
    return userRepository.findBySportName(sportName)
        .orElseThrow(() -> new RuntimeException("No such user!"));
  }

  public User updateUserWithSport(UUID id, Sport sport) {
    var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("No such user!"));
    user.setSport(sport);
    return userRepository.save(user);
  }

  public List<User> findByQuery(String query) {
    return userRepository.findByQuery(query);
  }

}
