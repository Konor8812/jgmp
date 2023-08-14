package com.illia.dao;

import com.illia.model.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountDAO extends CrudRepository<UserAccount, Long> {

  void refill(Long id, long amount);
}
