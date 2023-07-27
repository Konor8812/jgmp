package com.illia.nosql.repository;

import com.illia.nosql.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CouchbaseRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  @Query("SELECT META().id AS __id, usr.* FROM `demo-bucket` AS usr WHERE usr.sport.sportName = $1")
  Optional<User> findBySportName(String sportName);

  @Query("#{#n1ql.selectEntity} WHERE SEARCH(`demo-bucket`, $1)")
  List<User> findByQuery(String query);

}
