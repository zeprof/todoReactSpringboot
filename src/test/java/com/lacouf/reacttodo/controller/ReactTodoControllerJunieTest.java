package com.lacouf.reacttodo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.reacttodo.model.Todo;
import com.lacouf.reacttodo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DisplayName("ReactTodoController Test Suite using MockMvcTester")
public class ReactTodoControllerJunieTest {

    private MockMvcTester mockMvcTester;
    private TodoService todoService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void configureSystemUnderTest() {
        todoService = Mockito.mock(TodoService.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ReactTodoController(todoService)).build();
        mockMvcTester = MockMvcTester.create(mockMvc);
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return all todos")
    void getAllTodosTest() {
        // Arrange
        List<Todo> todoList = getTodoList();
        when(todoService.getAllTodosAndSetRemindersToFalse()).thenReturn(todoList);

        // Act
        MockMvcTester.MockMvcRequestBuilder result = mockMvcTester.get().uri("/todos").accept(MediaType.APPLICATION_JSON);

        // Assert
        assertThat(result).hasStatusOk();
        assertThat(result).bodyJson().hasPath("$[0].id");
        assertThat(result).bodyJson().hasPath("$[0].description");
        assertThat(result).bodyJson().hasPath("$[1].id");
        assertThat(result).bodyJson().hasPath("$[2].id");
    }

    @Test
    @DisplayName("Should return a specific todo by ID")
    void getTodoByIdTest() {
        // Arrange
        Todo todo = Todo.builder().id(1L).description("todo1").zedate("Aujourd'hui").reminder(false).build();
        when(todoService.findById(1L)).thenReturn(Optional.of(todo));

        // Act
        MockMvcTester.MockMvcRequestBuilder result = mockMvcTester.get().uri("/todos/1").accept(MediaType.APPLICATION_JSON);

        // Assert
        assertThat(result).hasStatus(HttpStatus.CREATED);
        assertThat(result).bodyJson().hasPath("$.id");
        assertThat(result).bodyJson().hasPath("$.description");
        assertThat(result).bodyJson().hasPath("$.zedate");
        assertThat(result).bodyJson().hasPath("$.reminder");
    }

    @Test
    @DisplayName("Should return conflict status when todo not found")
    void getTodoByIdNotFoundTest() {
        // Arrange
        when(todoService.findById(999L)).thenReturn(Optional.empty());

        // Act
        MockMvcTester.MockMvcRequestBuilder result = mockMvcTester.get().uri("/todos/999").accept(MediaType.APPLICATION_JSON);

        // Assert
        assertThat(result).hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("Should create a new todo")
    void createTodoTest() throws Exception {
        // Arrange
        Todo newTodo = Todo.builder().description("New Todo").zedate("Tomorrow").reminder(true).build();
        Todo savedTodo = Todo.builder().id(4L).description("New Todo").zedate("Tomorrow").reminder(true).build();
        when(todoService.saveTodo(any(Todo.class))).thenReturn(Optional.of(savedTodo));

        // Act
        MockMvcTester.MockMvcRequestBuilder result = mockMvcTester.post().uri("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTodo));

        // Assert
        assertThat(result).hasStatus(HttpStatus.CREATED);
        assertThat(result).bodyJson().hasPath("$.id");
        assertThat(result).bodyJson().hasPath("$.description");
        assertThat(result).bodyJson().hasPath("$.zedate");
        assertThat(result).bodyJson().hasPath("$.reminder");
    }

    @Test
    @DisplayName("Should return not found when todo creation fails")
    void createTodoFailTest() throws Exception {
        // Arrange
        Todo newTodo = Todo.builder().description("New Todo").zedate("Tomorrow").reminder(true).build();
        when(todoService.saveTodo(any(Todo.class))).thenReturn(Optional.empty());

        // Act
        MockMvcTester.MockMvcRequestBuilder result = mockMvcTester.post().uri("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTodo));

        // Assert
        assertThat(result).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Should update an existing todo")
    void updateTodoTest() throws Exception {
        // Arrange
        Todo updatedTodo = Todo.builder().id(1L).description("Updated Todo").zedate("Yesterday").reminder(false).build();
        when(todoService.saveTodo(any(Todo.class))).thenReturn(Optional.of(updatedTodo));

        // Act
        MockMvcTester.MockMvcRequestBuilder result = mockMvcTester.put().uri("/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTodo));

        // Assert
        assertThat(result).hasStatusOk();
        assertThat(result).bodyJson().hasPath("$.id");
        assertThat(result).bodyJson().hasPath("$.description");
        assertThat(result).bodyJson().hasPath("$.zedate");
        assertThat(result).bodyJson().hasPath("$.reminder");
    }

    @Test
    @DisplayName("Should return bad request when todo update fails")
    void updateTodoFailTest() throws Exception {
        // Arrange
        Todo updatedTodo = Todo.builder().id(999L).description("Updated Todo").zedate("Yesterday").reminder(false).build();
        when(todoService.saveTodo(any(Todo.class))).thenReturn(Optional.empty());

        // Act
        MockMvcTester.MockMvcRequestBuilder result = mockMvcTester.put().uri("/todos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTodo));

        // Assert
        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should delete a todo")
    void deleteTodoTest() {
        // Arrange
        doNothing().when(todoService).delete(anyLong());

        // Act
        MockMvcTester.MockMvcRequestBuilder result = mockMvcTester.delete().uri("/todos/1");

        // Assert
        assertThat(result).hasStatusOk();
    }

    private List<Todo> getTodoList() {
        List<Todo> todoList = new ArrayList<>();
        todoList.add(Todo.builder().id(1L).description("todo1").zedate("Aujourd'hui").reminder(false).build());
        todoList.add(Todo.builder().id(2L).description("todo2").zedate("Hier").reminder(true).build());
        todoList.add(Todo.builder().id(3L).description("todo3").zedate("Demain").reminder(false).build());
        return todoList;
    }
}
