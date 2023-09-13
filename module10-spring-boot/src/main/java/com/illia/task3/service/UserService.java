package com.illia.task3.service;


import com.illia.task3.model.security.Role;
import com.illia.task3.model.security.Role.RoleType;
import com.illia.task3.model.security.User;
import com.illia.task3.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public User saveNewUser(String username, String password) {
    if (userRepository.findByUsername(username) != null) {
      throw new EntityExistsException("User with such name already exists");
    }
    var user = User.builder().username(username).password(passwordEncoder.encode(password))
        .roles(Set.of(Role.builder()
            .id(1)
            .role(RoleType.USER)
            .build())).build();
    return userRepository.save(user);
  }
}
