package com.illia.controller;

import com.illia.controller.dto.EventResource;
import com.illia.model.Event;
import com.illia.service.EventService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventServiceController {

  private final EventService eventService;

  @ApiResponse(responseCode = "200",
      content = {@Content(schema = @Schema(implementation = Event.class))},
      description = "Successful response containing an event info")
  @GetMapping("/event/{id}")
  public ResponseEntity<EventResource> getEventById(@PathVariable Long id) {
    return ResponseEntity.ok(new EventResource(eventService.getEvent(id)));
  }

  @ApiResponse(responseCode = "200",
      content = {@Content(schema = @Schema(implementation = Event.class, type = "array"))},
      description = "Successful response containing a list of events")
  @GetMapping("/event")
  public ResponseEntity<List<Event>> getAllEvents() {
    return ResponseEntity.ok(eventService.getAllEvents());
  }

  @GetMapping("/event//title/{title}")
  public ResponseEntity<List<Event>> getAllEventsByTitle(@PathVariable String title) {
    return ResponseEntity.ok(eventService.getAllEventsByTitle(title));
  }

  @PostMapping("/event")
  public ResponseEntity<Event> createEvent(@RequestBody Event event) {
    return ResponseEntity.ok(eventService.createEvent(event));
  }

  @PutMapping("/event")
  public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
    return ResponseEntity.ok(eventService.updateEvent(event));
  }

  @DeleteMapping("/event/{id}")
  public ResponseEntity<Event> deleteEventById(@PathVariable Long id) {
    return ResponseEntity.ok(eventService.deleteEvent(id));
  }
}
