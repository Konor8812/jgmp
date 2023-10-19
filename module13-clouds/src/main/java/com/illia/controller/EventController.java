package com.illia.controller;

import com.illia.facade.BookingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

  private final BookingFacade bookingFacade;

  @GetMapping("/{title}")
  public String getEventsByTitle(@PathVariable(name = "title") String title,
      Model model){
    var events = bookingFacade.getEventsByTitle(title, 10, 0);
    model.addAttribute("entities", events);
    return "representation";
  }

}
