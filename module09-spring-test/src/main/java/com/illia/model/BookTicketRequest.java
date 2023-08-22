package com.illia.model;

import com.illia.model.Ticket.Category;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookTicketRequest implements Serializable {

  private long userId;
  private long eventId;
  private int place;
  private Category category;

}
