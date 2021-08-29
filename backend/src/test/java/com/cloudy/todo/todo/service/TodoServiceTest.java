package com.cloudy.todo.todo.service;

import com.cloudy.todo.global.dto.PageResult;
import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.domain.TodoStatus;
import com.cloudy.todo.todo.dto.request.CreateTodoDTO;
import com.cloudy.todo.todo.dto.request.TodoWithoutChildrenDTO;
import com.cloudy.todo.todo.dto.response.TodoDTO;
import com.cloudy.todo.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    public void getAllTodosTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        Todo todo2 = new Todo("Todo2");
        Todo todo3 = new Todo("Todo3");
        todo3.setId(1L);  // set id reversely intentionally
        todo2.setId(2L);
        todo1.setId(3L);

        // when
        when(todoRepository.findAllByOrderByIdDesc()).thenReturn(List.of(todo3, todo2, todo1));
        List<TodoDTO> todos = todoService.getTodosOrdered();

        // then
        assertThat(todos.size()).isEqualTo(3);
        assertThat(todos.get(0).getContent()).isEqualTo("Todo3");
        assertThat(todos.get(1).getContent()).isEqualTo("Todo2");
        assertThat(todos.get(2).getContent()).isEqualTo("Todo1");
    }

    @Test
    public void getTodosContainingContentTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        Todo todo2 = new Todo("Hello Todo2");
        Todo todo3 = new Todo("Something");
        todo3.setId(1L);  // set id reversely intentionally
        todo2.setId(2L);
        todo1.setId(3L);

        // when
        when(todoRepository.findAllByContentContainingOrderByIdDesc("Todo")).thenReturn(List.of(todo2, todo1));
        List<TodoDTO> todos = todoService.getTodosOrdered("Todo");

        // then
        assertThat(todos.size()).isEqualTo(2);
        assertThat(todos.get(0).getContent()).isEqualTo("Hello Todo2");
        assertThat(todos.get(1).getContent()).isEqualTo("Todo1");
    }

    @Test
    public void getTodosByStatusTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        Todo todo2 = new Todo("Todo2");
        todo2.setId(2L);
        todo2.setStatus(TodoStatus.DONE);
        Todo todo3 = new Todo("Todo3");
        todo3.setId(3L);

        // when
        when(todoRepository.findAllByStatusOrderByIdDesc(TodoStatus.NOT_YET)).thenReturn(List.of(todo3, todo1));
        when(todoRepository.findAllByStatusOrderByIdDesc(TodoStatus.DONE)).thenReturn(List.of(todo2));
        List<TodoDTO> notYetTodos = todoService.getTodosOrdered(TodoStatus.NOT_YET);
        List<TodoDTO> doneTodos = todoService.getTodosOrdered(TodoStatus.DONE);

        // then
        assertThat(notYetTodos.size()).isEqualTo(2);
        assertThat(notYetTodos.get(0).getContent()).isEqualTo("Todo3");
        assertThat(notYetTodos.get(0).getStatus()).isEqualTo(TodoStatus.NOT_YET);
        assertThat(notYetTodos.get(1).getContent()).isEqualTo("Todo1");
        assertThat(notYetTodos.get(1).getStatus()).isEqualTo(TodoStatus.NOT_YET);

        assertThat(doneTodos.size()).isEqualTo(1);
        assertThat(doneTodos.get(0).getContent()).isEqualTo("Todo2");
        assertThat(doneTodos.get(0).getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test
    public void getTodosByCreatedDateTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        Todo todo2 = new Todo("Todo2");
        todo2.setId(2L);
        Todo todo3 = new Todo("Todo3");
        todo3.setId(3L);
        todo3.setCreatedAt(LocalDateTime.now().plusDays(1L));

        // when
        LocalDateTime from = LocalDate.now().atStartOfDay();
        LocalDateTime to = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        when(todoRepository.findAllByCreatedAtBetweenOrderByIdDesc(from, to)).thenReturn(List.of(todo2, todo1));

        List<TodoDTO> todos = todoService.getTodosOrdered(LocalDate.now());

        // then
        assertThat(todos.size()).isEqualTo(2);
        assertThat(todos.get(0).getContent()).isEqualTo("Todo2");
        assertThat(todos.get(0).getCreatedAt()).isAfter(LocalDate.now().atStartOfDay());
        assertThat(todos.get(0).getCreatedAt()).isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        assertThat(todos.get(1).getContent()).isEqualTo("Todo1");
        assertThat(todos.get(1).getCreatedAt()).isAfter(LocalDate.now().atStartOfDay());
        assertThat(todos.get(1).getCreatedAt()).isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    }

    @Test
    public void getTodosPageableTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        Todo todo2 = new Todo("Todo2");
        todo2.setId(2L);
        Todo todo3 = new Todo("Todo3");
        todo3.setId(3L);

        // when
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").descending());
        Page<Todo> todoPage = new PageImpl(List.of(todo3, todo2), pageable, 3L);  // PageImpl.content field takes 'this page' content

        when(todoRepository.findAll(pageable)).thenReturn(todoPage);

        PageResult<TodoDTO> todos = todoService.getTodosPageable(pageable);

        // then
        assertThat(todos.getSize()).isEqualTo(2);
        assertThat(todos.getTotalPages()).isEqualTo(2);
        assertThat(todos.getData().get(0).getContent()).isEqualTo("Todo3");
        assertThat(todos.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    public void getTodosByContentPageableTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        Todo todo2 = new Todo("Something");
        todo2.setId(2L);
        Todo todo3 = new Todo("Todo3");
        todo3.setId(3L);

        // when
        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").descending());
        Page<Todo> todoPage = new PageImpl(List.of(todo3), pageable, 2L);  // PageImpl.content field takes 'this page' content

        when(todoRepository.findAllByContentContaining("Todo", pageable)).thenReturn(todoPage);

        PageResult<TodoDTO> todos = todoService.getTodosPageable("Todo", pageable);

        // then
        assertThat(todos.getSize()).isEqualTo(1);
        assertThat(todos.getTotalPages()).isEqualTo(2);
        assertThat(todos.getData().get(0).getContent()).isEqualTo("Todo3");
        assertThat(todos.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    public void getTodosByStatusPageableTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        todo1.setStatus(TodoStatus.DONE);
        Todo todo2 = new Todo("Something");
        todo2.setId(2L);
        todo2.setStatus(TodoStatus.DONE);
        Todo todo3 = new Todo("Todo3");
        todo3.setId(3L);

        // when
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").descending());
        Page<Todo> todoPage = new PageImpl(List.of(todo3, todo1), pageable, 2L);  // PageImpl.content field takes 'this page' content

        when(todoRepository.findAllByStatus(TodoStatus.DONE, pageable)).thenReturn(todoPage);

        PageResult<TodoDTO> todos = todoService.getTodosPageable(TodoStatus.DONE, pageable);

        // then
        assertThat(todos.getSize()).isEqualTo(2);
        assertThat(todos.getTotalPages()).isEqualTo(1);
        assertThat(todos.getData().get(0).getContent()).isEqualTo("Todo3");
        assertThat(todos.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    public void getTodosByCreatedDatePageableTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        todo1.setCreatedAt(LocalDateTime.of(2021, Month.AUGUST, 20, 13, 10, 30));
        Todo todo2 = new Todo("Something");
        todo2.setId(2L);
        todo2.setCreatedAt(LocalDateTime.of(2021, Month.AUGUST, 20, 13, 30, 30));
        Todo todo3 = new Todo("Todo3");
        todo3.setId(3L);

        // when
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").descending());
        Page<Todo> todoPage = new PageImpl(List.of(todo2, todo1), pageable, 2L);  // PageImpl.content field takes 'this page' content

        LocalDate date = LocalDate.of(2021, Month.AUGUST, 20);
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = LocalDateTime.of(date, LocalTime.MAX);

        when(todoRepository.findAllByCreatedAtBetween(from, to, pageable)).thenReturn(todoPage);

        PageResult<TodoDTO> todos = todoService.getTodosPageable(TodoStatus.DONE, pageable);

        // then
        assertThat(todos.getSize()).isEqualTo(2);
        assertThat(todos.getTotalPages()).isEqualTo(1);
        assertThat(todos.getData().get(0).getContent()).isEqualTo("Todo3");
        assertThat(todos.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    public void createTodoTest() {
        // given
        CreateTodoDTO content = new CreateTodoDTO("Todo1");
        Todo todo = new Todo("Todo1");
        todo.setId(1L);

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
    public void setPrecedenceTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        Todo todo2 = new Todo("Todo2");
        Todo todo3 = new Todo("Todo3");
        todo1.setId(1L);
        todo2.setId(2L);
        todo3.setId(3L);

        // when
        TodoDTO todo = todoService.setPrecedence(todo3, List.of(todo1, todo2));

        // then
        assertThat(todo.getChildren().size()).isEqualTo(2);
        assertThat(todo.getChildren().stream().map(TodoWithoutChildrenDTO::getId).collect(Collectors.toList())).contains(1L, 2L);
    }

    @Test
    public void updateStatusTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);

        Todo todo2 = new Todo("Todo2");
        todo2.setId(2L);
        todo2.setStatus(TodoStatus.DONE);

        // when
        todoService.updateStatus(todo1, TodoStatus.DONE);
        todoService.updateStatus(todo2, TodoStatus.NOT_YET);

        // then
        assertThat(todo1.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(todo2.getStatus()).isEqualTo(TodoStatus.NOT_YET);
    }

    @Test
    public void updateStatusConflictTest() {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);

        Todo todo2 = new Todo("Todo2");
        todo2.setId(2L);
        todo2.setStatus(TodoStatus.DONE);

        // when

        // then
        assertThatThrownBy(() -> todoService.updateStatus(todo1, TodoStatus.NOT_YET))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("The todo is not done");

        assertThatThrownBy(() -> todoService.updateStatus(todo2, TodoStatus.DONE))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("The todo is already done");
    }

    @Test
    public void updateStatusConflictByRelationshipTest() {
        // given
        Todo parent = new Todo("Parent");
        parent.setId(1L);
        Todo child = new Todo("Child");
        child.setId(2L);

        // when
        child.setMyParent(parent);

        // then
        assertThatThrownBy(() -> todoService.updateStatus(parent, TodoStatus.DONE))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Child (id: 2) is not done");

        child.finish();
        parent.finish();
        assertThatThrownBy(() -> todoService.updateStatus(child, TodoStatus.NOT_YET))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Parent (id: 1) is done");
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