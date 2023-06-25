package com.illia.module03chatgpt.controller.exception;

public class InvalidTodoItemException extends RuntimeException {

  public InvalidTodoItemException(String message) {
    super(message);
  }
}