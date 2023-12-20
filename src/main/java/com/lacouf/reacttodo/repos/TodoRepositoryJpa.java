package com.lacouf.reacttodo.repos;

import com.lacouf.reacttodo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepositoryJpa extends JpaRepository<Todo, Long> {
}
