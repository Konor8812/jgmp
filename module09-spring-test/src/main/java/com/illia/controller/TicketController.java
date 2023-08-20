package com.illia.controller;

import com.illia.facade.BookingFacade;
import com.illia.model.BookTicketRequest;
import com.illia.model.Ticket;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

  private final BookingFacade bookingFacade;

  @GetMapping(value = "/user")
  public ResponseEntity<List<Ticket>> getTicketsByUser(@RequestParam(name = "userId") Long userId) {
    var tickets = bookingFacade.getBookedTickets(bookingFacade.getUserById(userId), 10, 0);
    return ResponseEntity.ok(tickets);
  }

  @GetMapping(value = "/event")
  public ResponseEntity<List<Ticket>> getTicketsByEvent(
      @RequestParam(name = "eventId") Long eventId) {
    var tickets = bookingFacade.getBookedTickets(bookingFacade.getEventById(eventId), 10, 0);
    return ResponseEntity.ok(tickets);
  }

  @PostMapping("/book")
  public ResponseEntity<String> bookTicket(@RequestBody BookTicketRequest bookTicketRequest) {
    bookingFacade.bookTicket(bookTicketRequest);
    return ResponseEntity.ok("Request sent");
  }

}
