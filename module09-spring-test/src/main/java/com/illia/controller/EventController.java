package com.illia.controller;

import com.illia.facade.BookingFacade;
import com.illia.model.Event;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

  private final BookingFacade bookingFacade;

  @GetMapping("/{title}")
  @ResponseBody
  public List<Event> getEventsByTitle(@PathVariable(name = "title") String title) {
    return bookingFacade.getEventsByTitle(title, 10, 0);
  }

}
