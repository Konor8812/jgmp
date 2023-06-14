package com.illia.module03chatgpt.exception;

public class TodoItemNotFoundException extends RuntimeException {
  public TodoItemNotFoundException(String message) {
    super(message);
  }
}
