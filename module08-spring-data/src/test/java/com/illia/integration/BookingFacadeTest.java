package com.illia.integration;


import static com.illia.integration.constants.TestConstants.EXISTING_EVENT_DATE;
import static com.illia.integration.constants.TestConstants.EXISTING_EVENT_ID;
import static com.illia.integration.constants.TestConstants.EXISTING_EVENT_TITLE;
import static com.illia.integration.constants.TestConstants.EXISTING_USER_EMAIL;
import static com.illia.integration.constants.TestConstants.EXISTING_USER_ID;
import static com.illia.integration.constants.TestConstants.EXISTING_USER_NAME;
import static com.illia.integration.constants.TestConstants.PLACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.illia.data.DataStorage;
import com.illia.facade.BookingFacade;
import com.illia.model.Event;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = {SpringExtension.class})
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class BookingFacadeTest {

  @Autowired
  BookingFacade facade;

  @Autowired
  DataStorage dataStorage;
  @AfterEach
  public void cleanStorage(){
    dataStorage.clean();
  }

  @Test
  public void shouldSaveUserToStorage() {
    var user = createUser(EXISTING_USER_ID, EXISTING_USER_NAME, EXISTING_USER_EMAIL);

    facade.createUser(user);
    var response = facade.getUserById(EXISTING_USER_ID);
    assertNotNull(response);
    assertEquals(user.getId(), response.getId());
    assertEquals(user.getName(), response.getName());
    assertEquals(user.getEmail(), response.getEmail());
  }

  @Test
  public void shouldCreateTicketBasedOnUserAndEvent_ThenDeleteTicket() {
    var user = createUser(EXISTING_USER_ID, EXISTING_USER_NAME, EXISTING_USER_EMAIL);
    var event = createEvent(EXISTING_EVENT_ID, EXISTING_EVENT_TITLE, EXISTING_EVENT_DATE);

    facade.createUser(user);
    facade.createEvent(event);

    facade.bookTicket(user.getId(), event.getId(), PLACE, Category.PREMIUM);

    var ticketByUser = facade.getBookedTickets(user, 1, 0).get(0);
    var ticketByEvent = facade.getBookedTickets(event, 1, 0).get(0);
    assertEquals(ticketByEvent.getId(), ticketByUser.getId());

    facade.cancelTicket(ticketByEvent.getId());
    assertTrue(facade.getBookedTickets(user, 1, 0).isEmpty());
    assertTrue(facade.getBookedTickets(event, 1, 0).isEmpty());
  }


  private Event createEvent(long id, String title, Date date) {
    return Event.builder()
        .id(id)
        .title(title)
        .date(date)
        .build();
  }

  private User createUser(long id, String name, String email) {
    return User.builder()
        .id(id)
        .name(name)
        .email(email)
        .build();
  }
}
