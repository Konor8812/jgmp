package com.epam.ld.module2.testing.constants;

public class TestConstants {

  public static final String SENDER_NAME = "SenderName";
  public static final String RECIPIENT_NAME = "RecipientName";
  public static final String SENDER_PLACEHOLDER = "#{sender}";
  public static final String RECIPIENT_PLACEHOLDER = "#{recipient}";
  public static final String NO_VALUE_FOR_PLACEHOLDER_EXCEPTION_MESSAGE = "No value provided for one or more placeholders";
  public static final String TEMPLATE_ENGINE_GENERATION_RESULT = "message";
  public static final String CLIENT_ADDRESSES = "addresses";
  public static final String MESSAGE_CONTENT = "Sincerely yours, #{sender}";
  public static final String MESSAGE_CONTENT_WITH_UNSET_PLACEHOLDERS = "#{content} #{sender}";
}
