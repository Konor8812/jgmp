package com.illia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illia.dao.UserDAO;
import com.illia.model.User;
import com.illia.service.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

  UserDAO userDAO;
  UserServiceImpl userService;

  @BeforeEach
  public void prepareService() {
    userDAO = mock(UserDAO.class);
    userService = new UserServiceImpl();
    userService.setUserDAO(userDAO);
  }

  private final List<User> usersList = getUsersList();


  @Test
  public void deleteUserShouldReturnTrueIfUserExists() {
    when(userDAO.deleteUser(any(Long.class)))
        .thenReturn(mock(User.class));
    assertTrue(userService.deleteUser(1L));
    verify(userDAO, times(1)).deleteUser(any(Long.class));
  }

  @Test
  public void deleteUserShouldReturnFalseIfNoUserExist() {
    when(userDAO.deleteUser(any(Long.class)))
        .thenReturn(null);
    assertFalse(userService.deleteUser(1L));
    verify(userDAO, times(1)).deleteUser(any(Long.class));
  }

  @Test
  public void updateUserShouldReturnReturnNullIfNoUserExisted() {
    var mock = mock(User.class);
    when(userDAO.updateUser(mock))
        .thenReturn(null);
    assertNull(userService.updateUser(mock));
    verify(userDAO, times(1)).updateUser(same(mock));
  }

  @Test
  public void updateUserShouldReturnReturnExistingUser() {
    var mock = mock(User.class);
    when(userDAO.updateUser(mock))
        .thenReturn(mock(User.class));
    assertNotEquals(mock, userService.updateUser(mock));
    verify(userDAO, times(1)).updateUser(same(mock));
  }

  @Test
  public void saveUserShouldReturnReturnNullIfNoUserExisted() {
    var mock = mock(User.class);
    when(userDAO.saveUser(mock))
        .thenReturn(null);
    assertNull(userService.createUser(mock));
    verify(userDAO, times(1)).saveUser(eq(mock));
  }

  @Test
  public void saveUserShouldReturnReturnExistingUser() {
    var mock = mock(User.class);
    when(userDAO.saveUser(mock))
        .thenReturn(mock(User.class));
    assertNotEquals(mock, userService.createUser(mock));
    verify(userDAO, times(1)).saveUser(same(mock));
  }

  @Test
  public void getUsersByNameShouldCallDAOAndFilterResult() {
    when(userDAO.getAllUsers()).thenReturn(usersList);
    assertEquals(1, userService.getUsersByName("Name 1").size());
    verify(userDAO, times(1)).getAllUsers();
  }


  @Test
  public void getUserByEmailShouldCallDAOAndFilterResult() {
    when(userDAO.findUserByEmail("Email 1")).thenReturn(usersList.get(1));
    assertNotNull(userService.getUserByEmail("Email 1"));
    verify(userDAO, times(1)).findUserByEmail("Email 1");
  }

  @Test
  public void getUserByIdShouldCallDAOAndFilterResult() {
    when(userDAO.getUserById(1L)).thenReturn(usersList.get(1));
    assertNotNull(userService.getUserById(1L));
    verify(userDAO, times(1)).getUserById(1L);
  }


  private List<User> getUsersList() {
    var list = new ArrayList<User>();

    for (int i = 0; i < 3; i++) {
      int finalI = i;
      list.add(new User() {
        @Override
        public long getId() {
          return finalI;
        }

        @Override
        public void setId(long id) {

        }

        @Override
        public String getName() {
          return "Name " + finalI;
        }

        @Override
        public void setName(String name) {

        }

        @Override
        public String getEmail() {
          return "Email " + finalI;
        }

        @Override
        public void setEmail(String email) {

        }
      });
    }
    return list;
  }

}

