package com.cloudy.todo.todo.service;

import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.dto.CreateTodoDTO;
import com.cloudy.todo.todo.dto.TodoDTO;
import com.cloudy.todo.todo.exception.TodoNotFoundException;
import com.cloudy.todo.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TodoServiceIntegrationTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;


    @Test
    public void createTodoTest() {
        // given
        CreateTodoDTO content = new CreateTodoDTO("Todo1");

        // when
        TodoDTO todo = todoService.createTodo(content);

        // then
        assertThat(todo.getContent()).isEqualTo("Todo1");
    }

    @Test
    public void getTodosTest() {
        // given
        todoRepository.save(new Todo("Todo1"));
        todoRepository.save(new Todo("Todo2"));
        todoRepository.save(new Todo("Todo3"));

        // when
        List<TodoDTO> todos = todoService.getTodosOrdered();

        // then
        assertThat(todos.size()).isEqualTo(3);
        assertThat(todos.get(0).getContent()).isEqualTo("Todo3");  // order by id desc
        assertThat(todos.get(1).getContent()).isEqualTo("Todo2");
        assertThat(todos.get(2).getContent()).isEqualTo("Todo1");
    }

    @Test
    public void setLinkTest() {
        // given
        Todo todo1 = todoRepository.save(new Todo("Todo1"));
        Todo todo2 = todoRepository.save(new Todo("Todo2"));
        Todo todo3 = todoRepository.save(new Todo("Todo3"));

        // when
        todoService.setLink(todo3, List.of(todo1, todo2));

        // then
        Todo todo = todoRepository.findById(todo3.getId())
            .orElseThrow(() -> new TodoNotFoundException("There is no Todo where id: " + todo3.getId()));

        assertThat(todo.getChildren().size()).isEqualTo(2);
        assertThat(todo.getChildren().stream().map(Todo::getId).collect(Collectors.toList())).contains(todo1.getId(), todo2.getId());
    }

    @Test
    public void deleteTodoTest() {
        // given
        Todo todo = todoRepository.save(new Todo("Todo1"));

        // when
        todoService.deleteTodo(todo.getId());

        // then
        List<Todo> all = (List) todoRepository.findAll();  // down casting

        assertThat(all.size()).isEqualTo(0);
    }

}