package com.illia.nosql.entity;

import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Data
@Document
public class Sport {
  @Id
  private UUID id;
  @Field
  private String sportName;
  @Field
  private SportProficiency sportProficiency;
}
