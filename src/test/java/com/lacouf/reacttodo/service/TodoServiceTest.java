package com.lacouf.reacttodo.service;

import com.lacouf.reacttodo.model.Todo;
import com.lacouf.reacttodo.repos.TodoRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepositoryJpa repository;

    @InjectMocks
    private TodoService service;

    @Test
    public void testGetAllTodosAndSetRemindersToFalse() {
        // Arrange
        when(repository.findAll()).thenReturn(getListOfTodos());

        // Act
        List<Todo> allTodos = service.getAllTodosAndSetRemindersToFalse();

        // Assert
        assertThat(allTodos.size()).isEqualTo(3);
        assertThat(allTodos.get(0).getDescription()).isEqualTo("todo1");

        assertThat(allTodos.get(0).isReminder()).isFalse();
        assertThat(allTodos.get(1).isReminder()).isFalse();
        assertThat(allTodos.get(2).isReminder()).isFalse();

        verify(repository, times(1)).findAll();
    }

    @Test
    void testSaveTodo1() {
        // Arrange
        Todo todoToSave = new Todo("todo1", "Aujourd'hui", true);
        Todo todoToReturn = new Todo(1l, "todo1", "Aujourd'hui", true);
        when(repository.save(todoToSave)).thenReturn(todoToReturn);

        // Act
        Optional<Todo> todoOptional = service.saveTodo(todoToSave);
        Todo todoResponse = todoOptional.get();

        // Assert
        assertThat(todoResponse.getId()).isEqualTo(1L);
    }

    private List<Todo> getListOfTodos() {
        List<Todo> todoList = new ArrayList<>();
        todoList.add(Todo.builder()
                .id(1L)
                .description("todo1")
                .zedate("Aujourd'hui")
                .reminder(true)
                .build());
        todoList.add(Todo.builder()
                .id(1L)
                .description("todo2")
                .zedate("Hier")
                .reminder(true)
                .build());
        todoList.add(Todo.builder()
                .id(3L)
                .description("todo3")
                .zedate("Demain")
                .reminder(true)
                .build());
        return todoList;
    }

}
