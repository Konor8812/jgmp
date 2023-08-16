package com.illia.controller;

import com.illia.facade.BookingFacade;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_PDF_VALUE)
  public String getAllAsPDF(@ModelAttribute(name = "userId") Long userId,
      Model model) {
    var tickets = bookingFacade.getBookedTickets(bookingFacade.getUserById(userId), 10, 0);
    model.addAttribute("entities", tickets);
    return "representation";
  }

  @PostMapping(value = "/preload")
  public String preload(@RequestPart(name = "dataBatch") MultipartFile multipartFile)
      throws IOException {
    bookingFacade.preloadTicketsFromInputStream(multipartFile.getInputStream());
    return "redirect:/ticket";
  }

  @PostMapping(value = "/preloadFromFileSystem")
  public String preloadFromFileSystem()
      throws JobExecutionException {
    bookingFacade.preloadTicketsFromFile();
    return "redirect:/ticket";
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
