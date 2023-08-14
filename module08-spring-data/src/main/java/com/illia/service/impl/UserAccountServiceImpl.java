package com.illia.service.impl;

import com.illia.dao.UserAccountDAO;
import com.illia.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

  private final UserAccountDAO userAccountDAO;

  @Override
  public void refill(Long id, long amount) {
    userAccountDAO.refill(id, amount);
  }
}
