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
    when(userDAO.delete(any(Long.class)))
        .thenReturn(mock(User.class));
    assertTrue(userService.deleteUser(1L));
    verify(userDAO, times(1)).delete(any(Long.class));
  }

  @Test
  public void deleteUserShouldReturnFalseIfNoUserExist() {
    when(userDAO.delete(any(Long.class)))
        .thenReturn(null);
    assertFalse(userService.deleteUser(1L));
    verify(userDAO, times(1)).delete(any(Long.class));
  }

  @Test
  public void updateUserShouldReturnReturnNullIfNoUserExisted() {
    var mock = mock(User.class);
    when(userDAO.update(mock))
        .thenReturn(null);
    assertNull(userService.updateUser(mock));
    verify(userDAO, times(1)).update(same(mock));
  }

  @Test
  public void updateUserShouldReturnReturnExistingUser() {
    var mock = mock(User.class);
    when(userDAO.update(mock))
        .thenReturn(mock(User.class));
    assertNotEquals(mock, userService.updateUser(mock));
    verify(userDAO, times(1)).update(same(mock));
  }

  @Test
  public void saveUserShouldReturnReturnNullIfNoUserExisted() {
    var mock = mock(User.class);
    when(userDAO.save(mock))
        .thenReturn(null);
    assertNull(userService.createUser(mock));
    verify(userDAO, times(1)).save(eq(mock));
  }

  @Test
  public void saveUserShouldReturnReturnExistingUser() {
    var mock = mock(User.class);
    when(userDAO.save(mock))
        .thenReturn(mock(User.class));
    assertNotEquals(mock, userService.createUser(mock));
    verify(userDAO, times(1)).save(same(mock));
  }

  @Test
  public void getUsersByNameShouldCallDAOAndFilterResult() {
    when(userDAO.getAll()).thenReturn(usersList);
    assertEquals(1, userService.getUsersByName("Name 1").size());
    verify(userDAO, times(1)).getAll();
  }


  @Test
  public void getUserByEmailShouldCallDAOAndFilterResult() {
    when(userDAO.findUserByEmail("Email 1")).thenReturn(usersList.get(1));
    assertNotNull(userService.getUserByEmail("Email 1"));
    verify(userDAO, times(1)).findUserByEmail("Email 1");
  }

  @Test
  public void getUserByIdShouldCallDAOAndFilterResult() {
    when(userDAO.get(1L)).thenReturn(usersList.get(1));
    assertNotNull(userService.getUserById(1L));
    verify(userDAO, times(1)).get(1L);
  }


  private List<User> getUsersList() {
    var list = new ArrayList<User>();

    for (int i = 0; i < 3; i++) {
      list.add(User.builder()
                   .id(i)
                   .name("Name " + i)
                   .email("Email " + i)
          .build());

    }
    return list;
  }

}

