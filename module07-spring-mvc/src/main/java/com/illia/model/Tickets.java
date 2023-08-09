package com.illia.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
public class Tickets {

  private List<Ticket> ticketList;

  @XmlElement(name = "ticket")
  public List<Ticket> getTicketList() {
    return ticketList;
  }

  public void setTicketList(List<Ticket> ticketList) {
    this.ticketList = ticketList;
  }
}
