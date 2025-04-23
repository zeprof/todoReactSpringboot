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
        TcpServer.createTcpServer("todos");
        //repository.deleteAll();

        // save a couple of customers
        try {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // fetch all todo
        System.out.println("Todos avec findAll():");
        System.out.println("-------------------------------");
        for (Todo todo : repository.findAll()) {
            System.out.println(todo);
        }
        System.out.println();
    }
}
