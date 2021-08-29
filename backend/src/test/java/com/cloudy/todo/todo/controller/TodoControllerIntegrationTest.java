package com.cloudy.todo.todo.controller;

import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.domain.TodoStatus;
import com.cloudy.todo.todo.dto.request.CreateTodoDTO;
import com.cloudy.todo.todo.dto.response.TodoDTO;
import com.cloudy.todo.todo.repository.TodoRepository;
import com.cloudy.todo.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void GetTodosControllerIntegrationTest() throws Exception {
        // given
        CreateTodoDTO content1 = new CreateTodoDTO("Todo1");
        CreateTodoDTO content2 = new CreateTodoDTO("Todo2");

        // when
        TodoDTO todo1 = todoService.createTodo(content1);
        TodoDTO todo2 = todoService.createTodo(content2);

        // then
        mockMvc.perform(get("/api/v1/todos").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size", is(2)))
            .andExpect(jsonPath("$.data[0]['id']", is(todo2.getId().intValue())))  // order by id desc
            .andExpect(jsonPath("$.data[0]['content']", is("Todo2")))
            .andExpect(jsonPath("$.data[0]['status']", is("NOT_YET")))
            .andExpect(jsonPath("$.data[1]['id']", is(todo1.getId().intValue())))
            .andExpect(jsonPath("$.data[1]['content']", is("Todo1")))
            .andExpect(jsonPath("$.data[1]['status']", is("NOT_YET")))
            .andDo(print());
    }

    @Test
    public void GetTodosContainingContentControllerIntegrationTest() throws Exception {
        // given
        CreateTodoDTO content1 = new CreateTodoDTO("Todo1");
        CreateTodoDTO content2 = new CreateTodoDTO("Today Todo2 something");
        CreateTodoDTO content3 = new CreateTodoDTO("good luck");

        // when
        TodoDTO todo1 = todoService.createTodo(content1);
        TodoDTO todo2 = todoService.createTodo(content2);
        TodoDTO todo3 = todoService.createTodo(content3);

        // then
        mockMvc.perform(get("/api/v1/todos?content=Todo").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size", is(2)))
            .andExpect(jsonPath("$.data[0]['id']", is(todo2.getId().intValue())))  // order by id desc
            .andExpect(jsonPath("$.data[0]['content']", is("Today Todo2 something")))
            .andExpect(jsonPath("$.data[0]['status']", is("NOT_YET")))
            .andExpect(jsonPath("$.data[1]['id']", is(todo1.getId().intValue())))
            .andExpect(jsonPath("$.data[1]['content']", is("Todo1")))
            .andExpect(jsonPath("$.data[1]['status']", is("NOT_YET")))
            .andDo(print());
    }

    @Test
    public void GetTodosByStatusControllerIntegrationTest() throws Exception {
        // given

        // when
        TodoDTO todo1 = todoService.createTodo(new CreateTodoDTO("Todo1"));
        TodoDTO todo2 = todoService.createTodo(new CreateTodoDTO("Todo2"));
        TodoDTO todo3 = todoService.createTodo(new CreateTodoDTO("Todo3"));
        todoService.updateStatus(todo1.getId(), TodoStatus.DONE);

        // then
        mockMvc.perform(get("/api/v1/todos?status=DONE").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size", is(1)))
            .andExpect(jsonPath("$.data[0]['id']", is(todo1.getId().intValue())))
            .andExpect(jsonPath("$.data[0]['content']", is("Todo1")))
            .andExpect(jsonPath("$.data[0]['status']", is("DONE")))
            .andDo(print());

        mockMvc.perform(get("/api/v1/todos?status=NOT_YET").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size", is(2)))
            .andExpect(jsonPath("$.data[0]['id']", is(todo3.getId().intValue())))
            .andExpect(jsonPath("$.data[0]['content']", is("Todo3")))
            .andExpect(jsonPath("$.data[0]['status']", is("NOT_YET")))
            .andExpect(jsonPath("$.data[1]['id']", is(todo2.getId().intValue())))
            .andExpect(jsonPath("$.data[1]['content']", is("Todo2")))
            .andExpect(jsonPath("$.data[1]['status']", is("NOT_YET")));
    }

    @Test
    public void GetTodosByCreatedDateControllerIntegrationTest() throws Exception {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setCreatedAt(LocalDateTime.of(2021, Month.AUGUST, 20, 15, 30, 10));

        Todo todo2 = new Todo("Todo2");
        todo2.setCreatedAt(LocalDateTime.of(2021, Month.AUGUST, 20, 16, 30, 10));

        Todo todo3 = new Todo("Todo3");
        todo3.setCreatedAt(LocalDateTime.now().plusDays(1L));

        // when
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        // then
        mockMvc.perform(get("/api/v1/todos?createdDate=2021-08-20").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size", is(2)))
            .andExpect(jsonPath("$.data[0]['id']", is(2)))
            .andExpect(jsonPath("$.data[0]['content']", is("Todo2")))
            .andExpect(jsonPath("$.data[0]['status']", is("NOT_YET")))
            .andExpect(jsonPath("$.data[1]['id']", is(1)))
            .andExpect(jsonPath("$.data[1]['content']", is("Todo1")))
            .andExpect(jsonPath("$.data[1]['status']", is("NOT_YET")))
            .andDo(print());
    }

    @Test
    public void createTodoControllerIntegrationTest() throws Exception {
        // given

        // when

        // then
        mockMvc.perform(post("/api/v1/todos").content("{\"content\":\"Todo\"}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.content", is("Todo")))
            .andExpect(jsonPath("$.status", is("NOT_YET")))
            .andDo(print());
    }

    @Test
    public void setPrecedenceTodoControllerIntegrationTest() throws Exception {
        // given

        // when
        TodoDTO todo1 = todoService.createTodo(new CreateTodoDTO("Todo1"));
        TodoDTO todo2 = todoService.createTodo(new CreateTodoDTO("Todo2"));
        TodoDTO todo3 = todoService.createTodo(new CreateTodoDTO("Todo3"));

        // then
        mockMvc.perform(
                patch("/api/v1/todos/" + todo3.getId() + "/precedence")
                    .content("{\"precedenceIds\":[" + todo1.getId() + "," + todo2.getId() + "]}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.children.length()", is(2)))
            .andExpect(jsonPath("$.children[0]['id']").exists())
            .andExpect(jsonPath("$.children[1]['id']").exists())
            .andDo(print());
    }
}
