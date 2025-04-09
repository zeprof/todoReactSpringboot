package com.lacouf.reacttodo.controller;

import com.lacouf.reacttodo.model.Todo;
import com.lacouf.reacttodo.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class ReactTodoController {
    Logger logger = LoggerFactory.getLogger(ReactTodoController.class);

    private final TodoService todoService;

    public ReactTodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Todo>> getAllTodos() {
        logger.info("getAllTodos");
        return ResponseEntity.ok().body(todoService.getAllTodos());
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Todo> getTodo(@PathVariable Long id) {
        logger.info("getAllTodos id: " + id);
        return todoService.findById(id)
                .map(todo -> ResponseEntity.status(HttpStatus.CREATED).body(todo))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo newTodo) {
        logger.info("post - createTodo " + newTodo);
        return todoService.saveTodo(newTodo)
                .map(todo -> ResponseEntity.status(HttpStatus.CREATED).body(todo))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Todo> updateTodo(@RequestBody Todo newTodo, @PathVariable Long id) {
        logger.info("update - createTodo " + newTodo);
        return todoService.saveTodo(newTodo)
                .map(todo -> ResponseEntity.status(HttpStatus.CREATED).body(todo))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Todo> deleteTodo(@PathVariable Long id) {
        logger.info("delete - createTodo " + id);
        todoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
