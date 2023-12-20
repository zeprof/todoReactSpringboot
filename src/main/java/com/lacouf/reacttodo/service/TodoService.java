package com.lacouf.reacttodo.service;

import com.lacouf.reacttodo.model.Todo;
import com.lacouf.reacttodo.repos.TodoRepositoryJpa;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    public TodoRepositoryJpa repository;

    public TodoService(TodoRepositoryJpa repository) {
        this.repository = repository;
    }

    public List<Todo> getAllTodos() {
        return repository.findAll();
    }

    public Optional<Todo> saveTodo(Todo todo) {
        return Optional.of(repository.save(todo));
    }

    public Optional<Todo> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
