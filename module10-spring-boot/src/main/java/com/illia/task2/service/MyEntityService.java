package com.illia.task2.service;

import com.illia.task2.model.MyEntity;
import com.illia.task2.repository.EntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyEntityService {

  private final EntityRepository repository;

  public MyEntity createMyEntity(MyEntity myEntity){
    return repository.save(myEntity);
  }

  public MyEntity getMyEntity(long id){
    return repository.findById(id).orElseThrow(() -> new RuntimeException("No such entity"));
  }
  public MyEntity updateMyEntity(MyEntity myEntity){
    return repository.save(myEntity);
  }

  public void deleteMyEntity(MyEntity myEntity){
    repository.delete(myEntity);
  }
}
