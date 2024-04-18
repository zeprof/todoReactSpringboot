package com.lacouf.reacttodo;

import com.lacouf.reacttodo.model.Todo;
import com.lacouf.reacttodo.repos.TodoRepositoryJpa;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactTodoApplication implements CommandLineRunner {

    private final TodoRepositoryJpa repository;

    public ReactTodoApplication(TodoRepositoryJpa repository) {
        this.repository = repository;
    }
    public static void main(String[] args) {
        SpringApplication.run(ReactTodoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //repository.deleteAll();

        // save a couple of customers
        repository.save(Todo.builder()
                        .description("Todo1")
                        .zedate("Hier")
                        .reminder(false)
                        .build());

        repository.save(Todo.builder()
                .description("Todo2")
                .zedate("Aujourd'hui")
                .reminder(true)
                .build());

        // fetch all todo
        System.out.println("Todos avec findAll():");
        System.out.println("-------------------------------");
        for (Todo todo : repository.findAll()) {
            System.out.println(todo);
        }
        System.out.println();
    }
}
