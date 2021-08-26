package com.cloudy.todo.todo.service;

import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.domain.TodoStatus;
import com.cloudy.todo.todo.dto.CreateTodoDTO;
import com.cloudy.todo.todo.dto.TodoDTO;
import com.cloudy.todo.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;


    @Test
    public void createTodoTest() {
        // given
        CreateTodoDTO content = new CreateTodoDTO("Todo1");
        Todo todo = new Todo("Todo1");

        // when
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        TodoDTO savedTodo = todoService.createTodo(content);

        // then
        assertThat(savedTodo.getContent()).isEqualTo("Todo1");
        assertThat(savedTodo.getChildren().size()).isEqualTo(0);
        assertThat(savedTodo.getStatus()).isEqualTo(TodoStatus.NOT_YET);
        assertThat(savedTodo.getCreatedAt()).isNotNull();
    }

    @Test
    public void getTodosTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        Todo todo2 = new Todo("Todo2");
        Todo todo3 = new Todo("Todo3");
        todo3.setId(1L);  // set id reversely intentionally
        todo2.setId(2L);
        todo1.setId(3L);

        // when
        when(todoRepository.findAllByOrderByIdDesc()).thenReturn(List.of(todo3, todo2, todo1));
        List<TodoDTO> todos = todoService.getTodos();

        // then
        assertThat(todos.size()).isEqualTo(3);
        assertThat(todos.get(0).getContent()).isEqualTo("Todo3");
        assertThat(todos.get(1).getContent()).isEqualTo("Todo2");
        assertThat(todos.get(2).getContent()).isEqualTo("Todo1");
    }

    @Test
    public void setLinkTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        Todo todo2 = new Todo("Todo2");
        Todo todo3 = new Todo("Todo3");
        todo1.setId(1L);
        todo2.setId(2L);
        todo3.setId(3L);

        // when
        TodoDTO todo = todoService.setLink(todo3, List.of(todo1, todo2));

        // then
        assertThat(todo.getChildren().size()).isEqualTo(2);
        assertThat(todo.getChildren().stream().map(TodoDTO::getId).collect(Collectors.toList())).contains(1L, 2L);
    }

    @Test
    public void deleteTodoTest() {
        // given
        Long id = 100L;

        // when
        todoService.deleteTodo(id);

        // then
        verify(todoRepository).deleteById(id);
    }

}