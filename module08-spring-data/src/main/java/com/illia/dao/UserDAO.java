package com.illia.dao;

import com.illia.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User, Long> {

  User getUserByEmail(String email);

  List<User> getUsersByName(String name);

  User updateUser(User user);
}
