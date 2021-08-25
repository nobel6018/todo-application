package com.cloudy.todo.todo.controller;

import com.cloudy.todo.todo.dto.CreateTodoDTO;
import com.cloudy.todo.todo.dto.LinkTodoDTO;
import com.cloudy.todo.todo.dto.TodoDTO;
import com.cloudy.todo.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/api/v1/todos")
    public ResponseEntity<List<TodoDTO>> getTodos() {
        List<TodoDTO> todos = todoService.getTodos();

        return ResponseEntity.ok(todos);
    }

    @PostMapping("/api/v1/todos")
    public ResponseEntity<TodoDTO> createTodo(
        @RequestBody @Validated CreateTodoDTO createTodo
    ) {
        TodoDTO createdTodo = todoService.createTodo(createTodo);

        return ResponseEntity.ok(createdTodo);
    }

    @PatchMapping("/api/v1/todos/{todoId}/link")
    public ResponseEntity<TodoDTO> linkTodo(
        @PathVariable("todoId") Long parentTodoId,
        @RequestBody @Validated LinkTodoDTO linkTodo
    ) {
        TodoDTO linkedTodo = todoService.setLink(parentTodoId, linkTodo.getChildrenIds());

        return ResponseEntity.ok(linkedTodo);
    }
}
