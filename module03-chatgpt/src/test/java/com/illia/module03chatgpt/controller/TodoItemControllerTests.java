package com.illia.module03chatgpt.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.illia.module03chatgpt.controller.dto.TodoItemDTO;
import com.illia.module03chatgpt.controller.exception.InvalidTodoItemException;
import com.illia.module03chatgpt.exception.TodoItemNotFoundException;
import com.illia.module03chatgpt.service.TodoItemService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TodoItemController.class)
public class TodoItemControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TodoItemService todoItemService;
  private static ObjectMapper objectMapper;

  @BeforeAll
  static void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Test
  public void getAllTodoItems_ReturnsListOfItems() throws Exception {
    // Arrange
    TodoItemDTO item1 = new TodoItemDTO(1L, "Task 1", "Description 1");
    TodoItemDTO item2 = new TodoItemDTO(2L, "Task 2", "Description 2");
    List<TodoItemDTO> itemList = Arrays.asList(item1, item2);

    when(todoItemService.getAllTodoItems()).thenReturn(itemList);

    // Act & Assert
    mockMvc.perform(get("/api/todo-items")) // Corrected URL: /api/todo-items
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].title").value("Task 1"))
        .andExpect(jsonPath("$[0].description").value("Description 1"))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].title").value("Task 2"))
        .andExpect(jsonPath("$[1].description").value("Description 2"));
  }

  @Test
  public void getTodoItemById_ValidId_ReturnsItem() throws Exception {
    // Arrange
    Long itemId = 1L; // Set the valid item ID

    // Create a mock TodoItemDTO object
    TodoItemDTO itemDTO = new TodoItemDTO();
    itemDTO.setId(itemId);
    itemDTO.setTitle("Sample Item");
    itemDTO.setDescription("This is a sample item");

    // Mock the TodoItemService method to return the mock item DTO
    Mockito.when(todoItemService.getTodoItemById(itemId)).thenReturn(itemDTO);

    // Act
    mockMvc.perform(MockMvcRequestBuilders.get("/api/todo-items/{id}", itemId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemId))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Sample Item"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("This is a sample item"));

    // Assert
    verify(todoItemService, times(1)).getTodoItemById(itemId);
  }

  @Test
  public void getTodoItemById_InvalidId_ReturnsNotFound() throws Exception {
    // Arrange
    Long invalidId = 100L; // Set an invalid item ID that doesn't exist

    // Mock the TodoItemService method to throw an exception for the invalid item ID
    Mockito.when(todoItemService.getTodoItemById(invalidId))
        .thenThrow(new TodoItemNotFoundException("Todo item not found with ID: " + invalidId));

    // Act
    mockMvc.perform(get("/api/todo-items/{id}", invalidId))
        .andExpect(status().isNotFound());

    // Assert
    verify(todoItemService, times(1)).getTodoItemById(invalidId);
  }

  @Test
  void createTodoItem_ValidItem_ReturnsCreatedItem() throws Exception {
    // Mock the behavior of the todoItemService
    TodoItemDTO mockCreatedItemDTO = new TodoItemDTO();
    mockCreatedItemDTO.setId(1L);
    mockCreatedItemDTO.setTitle("Sample Todo Item");
    mockCreatedItemDTO.setDescription("Sample Description");
    when(todoItemService.createTodoItem(any(TodoItemDTO.class))).thenReturn(mockCreatedItemDTO);

    // Create a request body for the POST request
    TodoItemDTO requestDto = new TodoItemDTO();
    requestDto.setTitle("Sample Todo Item");
    requestDto.setDescription("Sample Description");
    String requestBody = objectMapper.writeValueAsString(requestDto);

    // Send POST request
    MvcResult result = mockMvc.perform(post("/api/todo-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    // Verify the response body
    String responseBody = result.getResponse().getContentAsString();
    TodoItemDTO responseDto = objectMapper.readValue(responseBody, TodoItemDTO.class);
    assertEquals(1L, responseDto.getId());
    assertEquals("Sample Todo Item", responseDto.getTitle());
    assertEquals("Sample Description", responseDto.getDescription());

    // Verify that the todoItemService was called with the correct argument
    verify(todoItemService, times(1)).createTodoItem(any(TodoItemDTO.class));
  }

  @Test
  void createTodoItem_InvalidItem_ReturnsBadRequest() throws Exception {
    // Mocking the service to throw InvalidTodoItemException
    doThrow(new InvalidTodoItemException("Invalid todo item"))
        .when(todoItemService).createTodoItem(any());

    // Test case for creating an item with invalid input
    TodoItemDTO invalidItemDTO = new TodoItemDTO(null, "Buy groceries", null);
    String invalidRequestBody = objectMapper.writeValueAsString(invalidItemDTO);

    mockMvc.perform(post("/api/todo-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidRequestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteTodoItem_ValidId_ReturnsNoContent() throws Exception {
    // Mock the behavior of the todoItemService
    Long itemId = 1L;
    doNothing().when(todoItemService).deleteTodoItem(itemId);

    // Send DELETE request
    mockMvc.perform(delete("/api/todo-items/{id}", itemId))
        .andExpect(status().isNoContent());

    // Verify that the todoItemService was called with the correct argument
    verify(todoItemService, times(1)).deleteTodoItem(itemId);
  }

  @Test
  public void updateTodoItem_ValidId_ReturnsUpdatedItem() throws Exception {
    // Prepare test data
    Long itemId = 1L;
    TodoItemDTO todoItemDTO = new TodoItemDTO();
    todoItemDTO.setTitle("Updated Title");

    // Set up the mock behavior
    TodoItemDTO updatedTodoItem = new TodoItemDTO();
    updatedTodoItem.setId(itemId);
    updatedTodoItem.setTitle("Updated Title");
    when(todoItemService.updateTodoItem(eq(itemId), any(TodoItemDTO.class))).thenReturn(
        updatedTodoItem);

    // Perform the request and assert the response
    mockMvc.perform(MockMvcRequestBuilders.put("/api/todo-items/{id}", itemId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\": \"Updated Title\"}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(itemId))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"));
  }


}

