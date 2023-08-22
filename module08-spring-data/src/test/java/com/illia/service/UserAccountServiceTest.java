package com.illia.service;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.illia.dao.UserAccountDAO;
import com.illia.service.impl.UserAccountServiceImpl;
import org.junit.jupiter.api.Test;

public class UserAccountServiceTest {

  UserAccountDAO userAccountDAO = mock(UserAccountDAO.class);
  UserAccountService userAccountService = new UserAccountServiceImpl(userAccountDAO);


  @Test
  public void refillOperationShouldCallDAO() {
    var id = 1L;
    var amount = 10L;
    userAccountService.refill(id, amount);
    verify(userAccountDAO, times(1)).refill(eq(id), eq(amount));
  }

}
