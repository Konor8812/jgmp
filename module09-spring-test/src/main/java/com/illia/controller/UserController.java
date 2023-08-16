package com.illia.controller;

import com.illia.facade.BookingFacade;
import com.illia.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    var user = bookingFacade.createUser(createUser(id, name, email));
    model.addAttribute("entity", user);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable final Long id,
      Model model) {
    var user = bookingFacade.deleteUser(id);
    model.addAttribute("entity", user);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/email/{email}")
  public String getByEmail(@PathVariable final String email,
      Model model) {
    var user = bookingFacade.getUserByEmail(email);
    model.addAttribute("entity", user);
    return ResponseEntity.ok(user);
  }

}
