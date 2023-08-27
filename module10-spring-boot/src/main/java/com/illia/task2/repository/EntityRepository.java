package com.illia.task2.repository;

import com.illia.task2.model.MyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends CrudRepository<MyEntity, Long> {

}
