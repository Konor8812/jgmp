package com.illia.controller;

import com.illia.facade.BookingFacade;
import com.illia.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final BookingFacade bookingFacade;

  @GetMapping("/name")
  public ResponseEntity<List<User>> getAllByName(@RequestParam(name = "name") final String name,
      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(name = "pageNum", defaultValue = "0") int pageNum) {
    var users = bookingFacade.getUsersByName(name, pageSize, pageNum);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> get(@PathVariable final Long id) {
    var user = bookingFacade.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @PutMapping("")
  public ResponseEntity<User> update(@RequestBody User user) {
    var updatedUser = bookingFacade.updateUser(user);
    return ResponseEntity.ok(updatedUser);
  }

  @PostMapping("")
  public ResponseEntity<User> create(@RequestBody User user) {
    var createdUser = bookingFacade.createUser(user);
    return ResponseEntity.ok(createdUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable final Long id) {
    var user = bookingFacade.deleteUser(id);
    return ResponseEntity.ok("Successfully deleted");
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<User> getByEmail(@PathVariable final String email) {
    var user = bookingFacade.getUserByEmail(email);
    return ResponseEntity.ok(user);
  }

}
