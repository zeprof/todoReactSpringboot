package com.lacouf.reacttodo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacouf.reacttodo.model.Todo;
import com.lacouf.reacttodo.repos.TodoRepositoryJpa;
import com.lacouf.reacttodo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ReactTodoController.class)
public class ReactTodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @MockitoBean
    private TodoRepositoryJpa todoRepositoryJpa;

    @Test
    public void getAllTodosTest() throws Exception {
        // Arrange
        List<Todo> todoList = getTodoList();
        when(todoService.getAllTodos()).thenReturn(todoList);

        // Act
        MvcResult result = mockMvc
                .perform(get("/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        var actuals = new ObjectMapper().readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actuals.size()).isEqualTo(3);
    }

    @Test
    public void saveTodoTest() throws Exception {
        // Arrange
        Todo expected = Todo.builder().id(1l).description("un todo").zedate("hier").reminder(false).build();
        when(todoService.saveTodo(any(Todo.class))).thenReturn(Optional.of(expected));
        when(todoRepositoryJpa.save(expected)).thenReturn(expected);

        // Act
        String content = new ObjectMapper().writeValueAsString(expected);
        MvcResult result = mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();

        // Assert
        var actualTodo = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Todo.class);
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(expected).isEqualTo(actualTodo);
    }

    private List<Todo> getTodoList() {
        List<Todo> todoList = new ArrayList<>();
        todoList.add(Todo.builder().id(1l).description("todo1").zedate("Aujourd'hui").reminder(false).build());
        todoList.add(Todo.builder().id(2l).zedate("todo2").description("Hier").reminder(true).build());
        todoList.add(Todo.builder().id(3l).description("todo3").zedate("Demain").reminder(false).build());
        return todoList;
    }

}
