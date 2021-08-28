package com.cloudy.todo.todo.controller;

import com.cloudy.todo.global.dto.Result;
import com.cloudy.todo.todo.domain.TodoStatus;
import com.cloudy.todo.todo.dto.request.CreateTodoDTO;
import com.cloudy.todo.todo.dto.request.LinkTodoDTO;
import com.cloudy.todo.todo.dto.request.UpdateStatusTodoDTO;
import com.cloudy.todo.todo.dto.response.TodoDTO;
import com.cloudy.todo.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/api/v1/todos")
    public ResponseEntity<Result<List<TodoDTO>>> getTodos(
        @RequestParam(required = false) String content,
        @RequestParam(required = false) TodoStatus status,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate createdDate
    ) {
        List<TodoDTO> todos;
        if (content != null) {
            todos = todoService.getTodosOrdered(content);
        } else if (status != null) {
            todos = todoService.getTodosOrdered(status);
        } else if (createdDate != null) {
            todos = todoService.getTodosOrdered(createdDate);
        } else {
            todos = todoService.getTodosOrdered();
        }

        return ResponseEntity.ok(new Result<>(todos, todos.size()));
    }

    @PostMapping("/api/v1/todos")
    public ResponseEntity<TodoDTO> createTodo(
        @RequestBody @Validated CreateTodoDTO createTodo
    ) {
        TodoDTO createdTodo = todoService.createTodo(createTodo);

        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    @PatchMapping("/api/v1/todos/{todoId}/link")
    public ResponseEntity<TodoDTO> linkTodo(
        @PathVariable("todoId") Long parentTodoId,
        @RequestBody @Validated LinkTodoDTO linkTodo
    ) {
        TodoDTO linkedTodo = todoService.setLink(parentTodoId, linkTodo.getChildrenIds());

        return ResponseEntity.ok(linkedTodo);
    }

    @PatchMapping("/api/v1/todos/{todoId}/status")
    public ResponseEntity<TodoDTO> updateTodoStatus(
        @PathVariable("todoId") Long todoId,
        @RequestBody UpdateStatusTodoDTO status
    ) {
        TodoDTO updatedTodo = todoService.updateStatus(todoId, status.getStatus());

        return ResponseEntity.ok(updatedTodo);
    }
}
