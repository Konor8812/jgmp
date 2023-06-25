package com.illia.module03chatgpt.controller;

import com.illia.module03chatgpt.controller.dto.TodoItemDTO;
import com.illia.module03chatgpt.exception.TodoItemNotFoundException;
import com.illia.module03chatgpt.service.TodoItemService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/todo-items")
public class TodoItemController {

  private final TodoItemService todoItemService;

  public TodoItemController(TodoItemService todoItemService) {
    this.todoItemService = todoItemService;
  }

  @GetMapping
  public ResponseEntity<List<TodoItemDTO>> getAllTodoItems() {
    List<TodoItemDTO> todoItems = todoItemService.getAllTodoItems();
    return ResponseEntity.ok(todoItems);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TodoItemDTO> getTodoItemById(@PathVariable Long id) {
    TodoItemDTO item;
    try {
      item = todoItemService.getTodoItemById(id);
    } catch (TodoItemNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo item not found", e);
    }
    return ResponseEntity.ok(item);
  }


  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public TodoItemDTO createTodoItem(@Valid @RequestBody TodoItemDTO todoItemDTO) {
    return todoItemService.createTodoItem(todoItemDTO);
  }


  @PutMapping("/{id}")
  public ResponseEntity<TodoItemDTO> updateTodoItem(
      @PathVariable("id") Long id, @RequestBody TodoItemDTO todoItemDTO) {
    TodoItemDTO updatedTodoItem = todoItemService.updateTodoItem(id, todoItemDTO);
    return ResponseEntity.ok(updatedTodoItem);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTodoItem(@PathVariable("id") Long id) {
    todoItemService.deleteTodoItem(id);
    return ResponseEntity.noContent().build();
  }
}
