package com.cloudy.todo.todo.service;

import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.dto.CreateTodoDTO;
import com.cloudy.todo.todo.dto.TodoDTO;
import com.cloudy.todo.todo.exception.TodoNotFoundException;
import com.cloudy.todo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<TodoDTO> getTodos() {
        List<Todo> todos = todoRepository.findAllByOrderByIdDesc();

        return todos.stream().map(Todo::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public TodoDTO createTodo(CreateTodoDTO createTodo) {
        Todo todo = new Todo(createTodo.getContent());
        return todoRepository.save(todo).toDTO();
    }

    @Transactional
    public TodoDTO setLink(Long parentId, List<Long> childrenIds) {
        Todo parent = todoRepository.findById(parentId)
            .orElseThrow(() -> new TodoNotFoundException("There is no Todo where id: " + parentId));
        Iterable<Todo> children = todoRepository.findAllById(childrenIds);

        return setLink(parent, children);
    }

    @Transactional
    public TodoDTO setLink(Todo parent, Iterable<Todo> children) {
        for (Todo child : children) {
            parent.addChildren(child);
        }

        return parent.toDTO();
    }

    @Transactional
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
