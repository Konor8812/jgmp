package com.illia.module03chatgpt.controller.advice;

import com.illia.module03chatgpt.controller.exception.InvalidTodoItemException;
import com.illia.module03chatgpt.exception.TodoItemNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(TodoItemNotFoundException.class)
  public ResponseEntity<Object> handleTodoItemNotFoundException(TodoItemNotFoundException ex) {
    String errorMessage = "Todo item not found";
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(errorMessage));
  }

  @ExceptionHandler(InvalidTodoItemException.class)
  public ResponseEntity<Object> handleInvalidTodoItemException(InvalidTodoItemException ex) {
    String errorMessage = "Invalid todo item";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(errorMessage));
  }

  private Map<String, String> createErrorResponse(String errorMessage) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("message", errorMessage);
    return errorResponse;
  }
}

