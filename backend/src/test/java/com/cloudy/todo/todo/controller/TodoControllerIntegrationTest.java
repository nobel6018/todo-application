package com.cloudy.todo.todo.controller;

import com.cloudy.todo.todo.dto.CreateTodoDTO;
import com.cloudy.todo.todo.dto.TodoDTO;
import com.cloudy.todo.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    public void GetTodosControllerIntegrationTest() throws Exception {
        // given
        CreateTodoDTO todo1 = new CreateTodoDTO("Todo1");
        CreateTodoDTO todo2 = new CreateTodoDTO("Todo2");

        // when
        todoService.createTodo(todo1);
        todoService.createTodo(todo2);

        // then
        mockMvc.perform(get("/api/v1/todos").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]['id']", is(2)))  // order by id desc
            .andExpect(jsonPath("$[0]['content']", is("Todo2")))
            .andExpect(jsonPath("$[0]['status']", is("NOT_YET")))
            .andExpect(jsonPath("$[1]['id']", is(1)))
            .andExpect(jsonPath("$[1]['content']", is("Todo1")))
            .andExpect(jsonPath("$[1]['status']", is("NOT_YET")))
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
    public void linkTodoControllerIntegrationTest() throws Exception {
        // given

        // when
        TodoDTO todo1 = todoService.createTodo(new CreateTodoDTO("Todo1"));
        TodoDTO todo2 = todoService.createTodo(new CreateTodoDTO("Todo2"));
        TodoDTO todo3 = todoService.createTodo(new CreateTodoDTO("Todo3"));

        // then
        mockMvc.perform(
                patch("/api/v1/todos/" + todo3.getId() + "/link")
                    .content("{\"childrenIds\":[" + todo1.getId() + "," + todo2.getId() + "]}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.children.length()", is(2)))
            .andExpect(jsonPath("$.children[0]['id']").exists())
            .andExpect(jsonPath("$.children[1]['id']").exists())
            .andDo(print());
    }
}
