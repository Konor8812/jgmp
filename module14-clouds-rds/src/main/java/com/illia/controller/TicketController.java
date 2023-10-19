package com.illia.controller;

import com.illia.facade.BookingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

  private final BookingFacade bookingFacade;

  @GetMapping("")
  public String welcome() {
    return "ticket";
  }

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getAllAsJSON(@ModelAttribute(name = "userId") Long userId,
      Model model) {
    var tickets = bookingFacade.getBookedTickets(bookingFacade.getUserById(userId), 10, 0);
    model.addAttribute("entities", tickets);
    return "representation";
  }

  @GetMapping(value = "/user")
  public String getTicketsByUser(@RequestParam(name = "userId") Long userId,
      Model model) {
    var tickets = bookingFacade.getBookedTickets(bookingFacade.getUserById(userId), 10, 0);
    model.addAttribute("entities", tickets);
    return "representation";
  }

  @GetMapping(value = "/event")
  public String welcome(@RequestParam(name = "eventId") Long eventId,
      Model model) {
    var tickets = bookingFacade.getBookedTickets(bookingFacade.getEventById(eventId), 10, 0);
    model.addAttribute("entities", tickets);
    return "representation";
  }
}
