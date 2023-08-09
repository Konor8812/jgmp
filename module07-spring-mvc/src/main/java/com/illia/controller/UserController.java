package com.illia.controller;

import com.illia.facade.BookingFacade;
import com.illia.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final BookingFacade bookingFacade;

  @GetMapping("")
  public String init() {
    return "user";
  }

  @GetMapping("/name")
  public String getAllByName(@ModelAttribute(name = "name") final String name,
      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
      Model model) {
    var users = bookingFacade.getUsersByName(name, pageSize, pageNum);
    model.addAttribute("entities", users);
    return "representation";
  }

  @GetMapping("/{id}")
  public String get(@PathVariable final Long id,
      Model model) {
    var user = bookingFacade.getUserById(id);
    model.addAttribute("entity", user);
    return "representation";
  }

  @PutMapping("")
  public String update(@ModelAttribute(name = "id") final Long id,
      @ModelAttribute(name = "name") final String name,
      @ModelAttribute(name = "email") final String email,
      Model model) {
    var user = bookingFacade.updateUser(createUser(id, name, email));
    model.addAttribute("entity", user);
    return "user";
  }

  @PostMapping("")
  public String create(@ModelAttribute(name = "id") final Long id,
      @ModelAttribute(name = "name") final String name,
      @ModelAttribute(name = "email") final String email,
      Model model) {
    var user = bookingFacade.createUser(createUser(id, name, email));
    model.addAttribute("entity", user);
    return "user";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable final Long id,
      Model model) {
    var user = bookingFacade.deleteUser(id);
    model.addAttribute("entity", user);
    return "user";
  }

  @GetMapping("/email/{email}")
  public String getByEmail(@PathVariable final String email,
      Model model) {
    var user = bookingFacade.getUserByEmail(email);
    model.addAttribute("entity", user);
    return "user";
  }

  @GetMapping("/back")
  public String returnToHome() {
    return "redirect:/home";
  }

  private User createUser(long id, String name, String email) {
    return new User(id, name, email);
  }

}
