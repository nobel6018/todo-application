package com.cloudy.todo.todo.service;

import com.cloudy.todo.global.dto.PageResult;
import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.domain.TodoStatus;
import com.cloudy.todo.todo.dto.request.CreateTodoDTO;
import com.cloudy.todo.todo.dto.response.TodoDTO;
import com.cloudy.todo.todo.exception.TodoNotFoundException;
import com.cloudy.todo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<TodoDTO> getTodosOrdered() {
        List<Todo> todos = todoRepository.findAllByOrderByIdDesc();

        return todos.stream().map(Todo::toDTO).collect(Collectors.toList());
    }

    public List<TodoDTO> getTodosOrdered(String content) {
        List<Todo> todos = todoRepository.findAllByContentContainingOrderByIdDesc(content);

        return todos.stream().map(Todo::toDTO).collect(Collectors.toList());
    }

    public List<TodoDTO> getTodosOrdered(TodoStatus status) {
        List<Todo> todos = todoRepository.findAllByStatusOrderByIdDesc(status);

        return todos.stream().map(Todo::toDTO).collect(Collectors.toList());
    }

    public List<TodoDTO> getTodosOrdered(LocalDate createdDate) {
        LocalDateTime from = createdDate.atStartOfDay();
        LocalDateTime to = LocalDateTime.of(createdDate, LocalTime.MAX);
        List<Todo> todos = todoRepository.findAllByCreatedAtBetweenOrderByIdDesc(from, to);

        return todos.stream().map(Todo::toDTO).collect(Collectors.toList());
    }

    public PageResult<TodoDTO> getTodosPageable(Pageable pageable) {
        Page<TodoDTO> todos = todoRepository.findAll(pageable)
            .map(Todo::toDTO);

        return new PageResult<>(
            todos.getContent(),
            todos.getSize(),
            todos.getTotalPages(),
            todos.getTotalElements(),
            todos.getNumberOfElements()
        );
    }

    public PageResult<TodoDTO> getTodosPageable(String content, Pageable pageable) {
        Page<TodoDTO> todos = todoRepository.findAllByContentContaining(content, pageable)
            .map(Todo::toDTO);

        return new PageResult<>(
            todos.getContent(),
            todos.getSize(),
            todos.getTotalPages(),
            todos.getTotalElements(),
            todos.getNumberOfElements()
        );
    }

    public PageResult<TodoDTO> getTodosPageable(TodoStatus todoStatus, Pageable pageable) {
        Page<TodoDTO> todos = todoRepository.findAllByStatus(todoStatus, pageable)
            .map(Todo::toDTO);

        return new PageResult<>(
            todos.getContent(),
            todos.getSize(),
            todos.getTotalPages(),
            todos.getTotalElements(),
            todos.getNumberOfElements()
        );
    }

    public PageResult<TodoDTO> getTodosPageable(LocalDate createdDate, Pageable pageable) {
        LocalDateTime from = createdDate.atStartOfDay();
        LocalDateTime to = LocalDateTime.of(createdDate, LocalTime.MAX);
        Page<TodoDTO> todos = todoRepository.findAllByCreatedAtBetween(from, to, pageable)
            .map(Todo::toDTO);

        return new PageResult<>(
            todos.getContent(),
            todos.getSize(),
            todos.getTotalPages(),
            todos.getTotalElements(),
            todos.getNumberOfElements()
        );
    }

    @Transactional
    public TodoDTO createTodo(CreateTodoDTO createTodo) {
        Todo todo = new Todo(createTodo.getContent());
        return todoRepository.save(todo).toDTO();
    }

    @Transactional
    public TodoDTO setPrecedence(Long followerId, List<Long> precedenceIds) {
        Todo parent = todoRepository.findById(followerId)
            .orElseThrow(() -> new TodoNotFoundException("There is no Todo where id: " + followerId));
        Iterable<Todo> children = todoRepository.findAllById(precedenceIds);

        return setPrecedence(parent, children);
    }

    @Transactional
    public TodoDTO setPrecedence(Todo parent, Iterable<Todo> children) {
        for (Todo child : children) {
            parent.addChildren(child);
        }

        return parent.toDTO();
    }

    @Transactional
    public TodoDTO updateStatus(Long id, TodoStatus status) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new TodoNotFoundException("There is no Todo where id: " + id));

        return updateStatus(todo, status);
    }

    @Transactional
    public TodoDTO updateStatus(Todo todo, TodoStatus status) {
        if (status == TodoStatus.DONE) {
            todo.finish();
        } else {
            todo.doing();
        }

        return todo.toDTO();
    }

    @Transactional
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
