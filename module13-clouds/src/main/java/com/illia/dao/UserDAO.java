package com.illia.dao;

import com.illia.data.DataStorage;
import com.illia.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDAO extends GenericDAO<User> {

  private final Logger logger = LoggerFactory.getLogger(TicketDAO.class);
  private final DataStorage<User> dataStorage;

  @Override
  public User save(User user) {
    logger.debug(String.format("Saving user with id=%s name=%s email=%s",
        user.getId(),
        user.getName(),
        user.getEmail()));
    return dataStorage.save(user);
  }

  @Override
  public User get(long id) {
    return dataStorage.get(id, User.class);
  }

  @Override
  public User update(User user) {
    return dataStorage.update(user);
  }

  @Override
  public User delete(long id) {
    return dataStorage.delete(id, User.class);
  }

  @Override
  public List<User> getAll() {
    return dataStorage.getAll(User.class);
  }

  public User findUserByEmail(String email) {
    return getAll().stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst().get();
  }
}
