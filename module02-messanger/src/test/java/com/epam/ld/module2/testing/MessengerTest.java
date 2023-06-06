package com.epam.ld.module2.testing;

import static com.epam.ld.module2.testing.constants.TestConstants.CLIENT_ADDRESSES;
import static com.epam.ld.module2.testing.constants.TestConstants.TEMPLATE_ENGINE_GENERATION_RESULT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;
import org.junit.jupiter.api.Test;

public class MessengerTest {

  @Test
  public void sendEmailTestShouldCallMailServerAndTemplateEngine(){
    var mailServer = mock(MailServer.class);
    var templateEngine = mock(TemplateEngine.class);
    var client = mock(Client.class);
    var template = mock(Template.class);

    var messenger = new Messenger(mailServer, templateEngine);
    messenger.sendMessage(client,template);

    verify(mailServer, times(1))
        .send(any(), any());
    verify(templateEngine, times(1))
        .generateMessage(same(template));
  }

  @Test
  public void sendEmailTestShouldCallMailServerWithExpectedArgs(){
    var mailServer = mock(MailServer.class);
    var templateEngine = mock(TemplateEngine.class);
    when(templateEngine.generateMessage(any(Template.class)))
        .thenReturn(TEMPLATE_ENGINE_GENERATION_RESULT);

    var client = new Client(); // kinda stub
    client.setAddresses(CLIENT_ADDRESSES);
    var template = mock(Template.class);

    var messenger = new Messenger(mailServer, templateEngine);
    messenger.sendMessage(client,template);

    verify(mailServer, times(1))
        .send(eq(CLIENT_ADDRESSES), eq(TEMPLATE_ENGINE_GENERATION_RESULT));
  }

}
