package com.illia.controller.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.illia.controller.EventServiceController;
import com.illia.model.Event;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

@Data
public class EventResource extends RepresentationModel<EventResource> {
  private Event event;

  public EventResource(Event event) {
    Link selfLink = linkTo(methodOn(EventServiceController.class).getEventById(event.getId())).withSelfRel();
    add(selfLink);
  }
}
