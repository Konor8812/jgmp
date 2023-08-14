package com.illia.integration;


import static com.illia.integration.constants.TestConstants.EVENT_PRICE;
import static com.illia.integration.constants.TestConstants.EXISTING_EVENT_DATE;
import static com.illia.integration.constants.TestConstants.EXISTING_EVENT_TITLE;
import static com.illia.integration.constants.TestConstants.EXISTING_USER_EMAIL;
import static com.illia.integration.constants.TestConstants.EXISTING_USER_NAME;
import static com.illia.integration.constants.TestConstants.FUNDS_AMOUNT;
import static com.illia.integration.constants.TestConstants.PLACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.illia.facade.BookingFacade;
import com.illia.model.Event;
import com.illia.model.Ticket.Category;
import com.illia.model.User;
import com.illia.model.UserAccount;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = {SpringExtension.class})
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
@Sql(scripts = "classpath:createTestDatabase.sql")
@DirtiesContext
public class BookingFacadeTest {

  @Autowired
  BookingFacade facade;

  @Test
  public void shouldSaveUserToStorage() {
    var user = createUser(EXISTING_USER_NAME, EXISTING_USER_EMAIL, 0);

    var response = facade.createUser(user);
    assertNotNull(response);
    assertNotEquals(0, response.getId());
    assertEquals(user.getName(), response.getName());
    assertEquals(user.getEmail(), response.getEmail());
  }

  @Test
  public void shouldCreateTicketBasedOnUserAndEvent_ThenDeleteTicket() {
    var user = createUser(EXISTING_USER_NAME, EXISTING_USER_EMAIL, FUNDS_AMOUNT);
    var event = createEvent(EXISTING_EVENT_TITLE, EXISTING_EVENT_DATE, EVENT_PRICE);

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


  private Event createEvent(String title, Date date, long price) {
    return Event.builder()
        .title(title)
        .date(date)
        .price(price)
        .build();
  }

  private User createUser(String name, String email, long fundsAmount) {
    return User.builder()
        .name(name)
        .email(email)
        .userAccount(UserAccount.builder()
            .prepaid(fundsAmount)
            .build())
        .build();
  }

  @Test
  public void shouldCreateUserAndEvent_ThenCreateTicketAndWithdrawPriceFromUsersFunds() {
    var user = createUser(EXISTING_USER_NAME, EXISTING_USER_EMAIL, FUNDS_AMOUNT);
    var event = createEvent(EXISTING_EVENT_TITLE, EXISTING_EVENT_DATE, EVENT_PRICE);

    facade.createUser(user);
    facade.createEvent(event);

    facade.bookTicket(user.getId(), event.getId(), PLACE, Category.PREMIUM);

    assertEquals(FUNDS_AMOUNT - EVENT_PRICE, user.getUserAccount().getPrepaid());
  }
}
