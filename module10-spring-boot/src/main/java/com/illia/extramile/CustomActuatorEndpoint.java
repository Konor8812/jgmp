package com.illia.extramile;

import com.illia.task3.repository.UserRepository;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "databaseCapacity")
@RequiredArgsConstructor
public class CustomActuatorEndpoint {

  private final UserRepository userRepository;

  @ReadOperation
  @Counted(value = "custom_method_invocation_counter", description = "custom counter")
  public Long getUsersAmount() {
    return userRepository.count();
  }
}
