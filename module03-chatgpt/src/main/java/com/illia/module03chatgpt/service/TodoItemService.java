package com.illia.module03chatgpt.service;

import com.illia.module03chatgpt.controller.dto.TodoItemDTO;
import com.illia.module03chatgpt.exception.TodoItemNotFoundException;
import com.illia.module03chatgpt.persistance.TodoItem;
import com.illia.module03chatgpt.persistance.repository.TodoItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TodoItemService {

  private final TodoItemRepository todoItemRepository;

  public TodoItemService(TodoItemRepository todoItemRepository) {
    this.todoItemRepository = todoItemRepository;
  }

  public List<TodoItemDTO> getAllTodoItems() {
    List<TodoItem> todoItems = todoItemRepository.findAll();
    return mapTodoItemsToDTOs(todoItems);
  }

  public TodoItemDTO getTodoItemById(Long id) {
    TodoItem todoItem = findTodoItemById(id);
    return mapTodoItemToDTO(todoItem);
  }

  public TodoItemDTO createTodoItem(TodoItemDTO todoItemDTO) {
    TodoItem todoItem = mapDTOToTodoItem(todoItemDTO);
    TodoItem savedTodoItem = todoItemRepository.save(todoItem);
    return mapTodoItemToDTO(savedTodoItem);
  }

  public TodoItemDTO updateTodoItem(Long id, TodoItemDTO todoItemDTO) {
    TodoItem todoItem = findTodoItemById(id);
    todoItem.setTitle(todoItemDTO.getTitle());
    todoItem.setDescription(todoItemDTO.getDescription());
    TodoItem updatedTodoItem = todoItemRepository.save(todoItem);
    return mapTodoItemToDTO(updatedTodoItem);
  }

  public void deleteTodoItem(Long id) {
    TodoItem todoItem = findTodoItemById(id);
    todoItemRepository.delete(todoItem);
  }

  private TodoItem findTodoItemById(Long id) {
    return todoItemRepository.findById(id)
        .orElseThrow(() -> new TodoItemNotFoundException("Todo item not found"));
  }

  private List<TodoItemDTO> mapTodoItemsToDTOs(List<TodoItem> todoItems) {
    return todoItems.stream()
        .map(this::mapTodoItemToDTO)
        .collect(Collectors.toList());
  }

  private TodoItemDTO mapTodoItemToDTO(TodoItem todoItem) {
    TodoItemDTO todoItemDTO = new TodoItemDTO();
    todoItemDTO.setId(todoItem.getId());
    todoItemDTO.setTitle(todoItem.getTitle());
    todoItemDTO.setDescription(todoItem.getDescription());
    return todoItemDTO;
  }

  private TodoItem mapDTOToTodoItem(TodoItemDTO todoItemDTO) {
    TodoItem todoItem = new TodoItem();
    todoItem.setTitle(todoItemDTO.getTitle());
    todoItem.setDescription(todoItemDTO.getDescription());
    return todoItem;
  }
}
