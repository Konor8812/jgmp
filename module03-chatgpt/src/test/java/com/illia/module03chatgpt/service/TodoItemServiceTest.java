package com.illia.module03chatgpt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.illia.module03chatgpt.controller.dto.TodoItemDTO;
import com.illia.module03chatgpt.exception.TodoItemNotFoundException;
import com.illia.module03chatgpt.persistance.TodoItem;
import com.illia.module03chatgpt.persistance.repository.TodoItemRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TodoItemServiceTest {

  @Mock
  private TodoItemRepository todoItemRepository;

  private TodoItemService todoItemService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    todoItemService = new TodoItemService(todoItemRepository);
  }

  @Test
  public void getAllTodoItems_ReturnsAllItems() {
    // Arrange
    TodoItem todoItem1 = new TodoItem();
    todoItem1.setId(1L);
    todoItem1.setTitle("Item 1");
    todoItem1.setDescription("Description 1");

    TodoItem todoItem2 = new TodoItem();
    todoItem2.setId(2L);
    todoItem2.setTitle("Item 2");
    todoItem2.setDescription("Description 2");

    List<TodoItem> todoItems = Arrays.asList(todoItem1, todoItem2);

    when(todoItemRepository.findAll()).thenReturn(todoItems);

    // Act
    List<TodoItemDTO> result = todoItemService.getAllTodoItems();

    // Assert
    assertEquals(todoItems.size(), result.size());
    // Add more assertions as needed
  }

  @Test
  public void getTodoItemById_ExistingId_ReturnsTodoItem() {
    // Arrange
    TodoItem todoItem = new TodoItem();
    todoItem.setId(1L);
    todoItem.setTitle("Item 1");
    todoItem.setDescription("Description 1");

    when(todoItemRepository.findById(1L)).thenReturn(Optional.of(todoItem));

    // Act
    TodoItemDTO result = todoItemService.getTodoItemById(1L);

    // Assert
    assertEquals(todoItem.getId(), result.getId());
    assertEquals(todoItem.getTitle(), result.getTitle());
    assertEquals(todoItem.getDescription(), result.getDescription());
  }

  @Test
  public void getTodoItemById_NonExistingId_ThrowsTodoItemNotFoundException() {
    // Arrange
    when(todoItemRepository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(TodoItemNotFoundException.class, () -> todoItemService.getTodoItemById(1L));
  }

  @Test
  void getTodoItemById_ExistingId_ReturnsTodoItemDTO() {
    // Arrange
    Long itemId = 1L;
    String title = "Buy groceries";
    String description = "Buy milk, eggs, and bread";

    TodoItem todoItem = new TodoItem();
    todoItem.setId(itemId);
    todoItem.setTitle(title);
    todoItem.setDescription(description);

    when(todoItemRepository.findById(itemId)).thenReturn(Optional.of(todoItem));

    // Act
    TodoItemDTO result = todoItemService.getTodoItemById(itemId);

    // Assert
    assertNotNull(result);
    assertEquals(itemId, result.getId());
    assertEquals(title, result.getTitle());
    assertEquals(description, result.getDescription());
  }

  @Test
  public void createTodoItem_ValidItem_ReturnsCreatedTodoItem() {
    // Arrange
    TodoItemDTO todoItemDTO = new TodoItemDTO(null, "Buy groceries",
        "Go to the store and buy groceries");
    TodoItem todoItem = new TodoItem();
    todoItem.setId(1L);
    todoItem.setTitle("Buy groceries");
    todoItem.setDescription("Go to the store and buy groceries");

    when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem);

    // Act
    TodoItemDTO result = todoItemService.createTodoItem(todoItemDTO);

    // Assert
    assertEquals(todoItem.getId(), result.getId());
    assertEquals(todoItem.getTitle(), result.getTitle());
    assertEquals(todoItem.getDescription(), result.getDescription());
  }

  @Test
  public void createTodoItem_InvalidItem_ThrowsInvalidTodoItemException() {
    // Arrange
    TodoItemDTO todoItemDTO = new TodoItemDTO(null, null, null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> todoItemService.createTodoItem(todoItemDTO));
  }

  @Test
  public void updateTodoItem_ExistingItem_ReturnsUpdatedTodoItem() {
    // Arrange
    Long itemId = 1L;
    TodoItemDTO todoItemDTO = new TodoItemDTO(itemId, "Buy groceries",
        "Go to the store and buy groceries");
    TodoItem existingTodoItem = new TodoItem();
    existingTodoItem.setId(itemId);
    existingTodoItem.setTitle("Old Title");
    existingTodoItem.setDescription("Old Description");
    TodoItem updatedTodoItem = new TodoItem();
    updatedTodoItem.setId(itemId);
    updatedTodoItem.setTitle(todoItemDTO.getTitle());
    updatedTodoItem.setDescription(todoItemDTO.getDescription());

    when(todoItemRepository.findById(itemId)).thenReturn(Optional.of(existingTodoItem));
    when(todoItemRepository.save(any(TodoItem.class))).thenReturn(updatedTodoItem);

    // Act
    TodoItemDTO result = todoItemService.updateTodoItem(itemId, todoItemDTO);

    // Assert
    assertEquals(updatedTodoItem.getId(), result.getId());
    assertEquals(updatedTodoItem.getTitle(), result.getTitle());
    assertEquals(updatedTodoItem.getDescription(), result.getDescription());
  }

  @Test
  public void updateTodoItem_NonExistingItem_ThrowsTodoItemNotFoundException() {
    // Arrange
    Long itemId = 1L;
    TodoItemDTO todoItemDTO = new TodoItemDTO(itemId, "Buy groceries",
        "Go to the store and buy groceries");

    when(todoItemRepository.findById(itemId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(TodoItemNotFoundException.class,
        () -> todoItemService.updateTodoItem(itemId, todoItemDTO));
  }
}
