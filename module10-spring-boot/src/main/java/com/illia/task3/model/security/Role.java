package com.illia.task3.model.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
public class Role implements GrantedAuthority {

  @Id
  @Column(name = "id")
  private long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private RoleType role;

  @Override
  public String getAuthority() {
    return this.role.name();
  }


  public enum RoleType {
    USER, ADMIN, DEVELOPER
  }
}
