package com.illia.dao;


import com.illia.data.DataStorage;
import com.illia.model.User;
import java.util.List;
import java.util.stream.Collectors;

public class UserDAO {

  private DataStorage dataStorage;
  private static final String NAMESPACE = "user:";

  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  public User saveUser(User user) {
    return (User) dataStorage.save(NAMESPACE + user.getId(), user);
  }

  public User getUserById(long id) {
    return (User) dataStorage.get(NAMESPACE + id);
  }

  public User updateUser(User user) {
    return (User) dataStorage.update(NAMESPACE + user.getId(), user);
  }

  public User deleteUser(long id) {
    return (User) dataStorage.delete(NAMESPACE + id);
  }

  public List<User> getAllUsers() {
    return dataStorage.getAll(NAMESPACE).stream().map(x -> (User) x).collect(Collectors.toList());
  }

  public User findUserByEmail(String email) {
    return getAllUsers().stream()
        .filter(x -> x.getEmail().equals(email))
        .findFirst().get();
  }
}
