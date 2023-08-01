package com.illia.nosql.controller;

import com.illia.nosql.entity.Sport;
import com.illia.nosql.entity.User;
import com.illia.nosql.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping(value = "/user/{id}")
  public ResponseEntity<User> getUserById(@PathVariable final UUID id) {
    return ResponseEntity.ok(userService.findUserById(id));
  }

  @GetMapping(value = "/user/email/{email}")
  public ResponseEntity<User> getUserByEmail(@PathVariable final String email) {
    return ResponseEntity.ok(userService.findUserByEmail(email));
  }

  @GetMapping(value = "/user/sport/{sport}")
  public ResponseEntity<User> getUserBySport(@PathVariable final String sport) {
    return ResponseEntity.ok(userService.findUserBySportName(sport));
  }

  @GetMapping(value = "/search/user")
  public ResponseEntity<List<User>> getUserByQuery(@RequestParam(name = "q") final String query) {
    return ResponseEntity.ok(userService.findByQuery(query));
  }

  @PostMapping(value = "/user")
  public ResponseEntity<User> createUser(@RequestBody final User user) {
    var createdUser = userService.createUser(user);
    return ResponseEntity.ok(createdUser);
  }


  @PutMapping(value = "/user/{id}/sport")
  public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody final Sport sport) {
    var updated = userService.updateUserWithSport(id, sport);
    return ResponseEntity.ok(updated);
  }
}
