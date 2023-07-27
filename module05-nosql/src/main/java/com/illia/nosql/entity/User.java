package com.illia.nosql.entity;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Data
@Document
public class User {

  @Id
  private UUID id;
  @Field
  private String email;
  @Field
  private String fullName;
  @Field
  private String birthDate;
  @Field
  private Gender gender;
  @Field
  private Sport sport;

}
