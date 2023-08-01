package com.illia.dao;

import com.illia.data.DataStorage;
import com.illia.model.User;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAO extends GenericDAO<User> {

  private final Logger logger = LoggerFactory.getLogger(TicketDAO.class);
  private DataStorage<User> dataStorage;

  public void setDataStorage(DataStorage<User> dataStorage) {
    this.dataStorage = dataStorage;
  }

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
