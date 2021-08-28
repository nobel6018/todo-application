package com.cloudy.todo.todo.controller;

import com.cloudy.todo.todo.domain.Todo;
import com.cloudy.todo.todo.domain.TodoStatus;
import com.cloudy.todo.todo.dto.request.CreateTodoDTO;
import com.cloudy.todo.todo.service.TodoService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    public void GetTodosControllerTest() throws Exception {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        Todo todo2 = new Todo("Todo2");
        todo2.setId(2L);

        // when
        when(todoService.getTodosOrdered()).thenReturn(List.of(todo2.toDTO(), todo1.toDTO()));

        // then
        mockMvc.perform(get("/api/v1/todos").contentType(MediaType.APPLICATION_JSON))
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
    public void GetTodosControllerContainingContentTest() throws Exception {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        Todo todo2 = new Todo("Hello Todo2");
        todo2.setId(2L);
        Todo todo3 = new Todo("Something");
        todo3.setId(3L);

        // when
        when(todoService.getTodosOrdered("Todo")).thenReturn(List.of(todo2.toDTO(), todo1.toDTO()));

        // then
        mockMvc.perform(get("/api/v1/todos?content=Todo").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size", is(2)))
            .andExpect(jsonPath("$.data[0]['id']", is(2)))
            .andExpect(jsonPath("$.data[0]['content']", is("Hello Todo2")))
            .andExpect(jsonPath("$.data[0]['status']", is("NOT_YET")))
            .andExpect(jsonPath("$.data[1]['id']", is(1)))
            .andExpect(jsonPath("$.data[1]['content']", is("Todo1")))
            .andExpect(jsonPath("$.data[1]['status']", is("NOT_YET")))
            .andDo(print());
    }

    @Test
    public void GetTodosControllerByStatusTest() throws Exception {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        todo1.setStatus(TodoStatus.DONE);
        Todo todo2 = new Todo("Todo2");
        todo2.setId(2L);
        Todo todo3 = new Todo("Todo3");
        todo3.setId(3L);

        // when
        when(todoService.getTodosOrdered(TodoStatus.NOT_YET)).thenReturn(List.of(todo3.toDTO(), todo2.toDTO()));
        when(todoService.getTodosOrdered(TodoStatus.DONE)).thenReturn(List.of(todo1.toDTO()));

        // then
        mockMvc.perform(get("/api/v1/todos?status=DONE").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size", is(1)))
            .andExpect(jsonPath("$.data[0]['id']", is(1)))
            .andExpect(jsonPath("$.data[0]['content']", is("Todo1")))
            .andExpect(jsonPath("$.data[0]['status']", is("DONE")))
            .andDo(print());

        mockMvc.perform(get("/api/v1/todos?status=NOT_YET").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size", is(2)))
            .andExpect(jsonPath("$.data[0]['id']", is(3)))
            .andExpect(jsonPath("$.data[0]['content']", is("Todo3")))
            .andExpect(jsonPath("$.data[0]['status']", is("NOT_YET")))
            .andExpect(jsonPath("$.data[1]['id']", is(2)))
            .andExpect(jsonPath("$.data[1]['content']", is("Todo2")))
            .andExpect(jsonPath("$.data[1]['status']", is("NOT_YET")))
            .andDo(print());
    }

    @Test
    public void GetTodosControllerByCreatedDateTest() throws Exception {
        // given
        Todo todo1 = new Todo("Todo1");
        todo1.setId(1L);
        todo1.setCreatedAt(LocalDateTime.of(2021, Month.AUGUST, 20, 15, 30, 10));
        Todo todo2 = new Todo("Todo2");
        todo2.setCreatedAt(LocalDateTime.of(2021, Month.AUGUST, 20, 16, 30, 10));
        todo2.setId(2L);
        Todo todo3 = new Todo("Todo3");
        todo3.setId(3L);
        todo3.setCreatedAt(LocalDateTime.now().plusDays(1L));

        // when
        when(todoService.getTodosOrdered(LocalDate.of(2021, Month.AUGUST, 20))).thenReturn(List.of(todo2.toDTO(), todo1.toDTO()));

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

    // Todo: implement, and test PATCH children link controller too
    @Test
    @Disabled
    public void createTodoBodyMissingControllerTest() throws Exception {
        // given
        CreateTodoDTO createTodo = new CreateTodoDTO("Todo");
        Todo todo = new Todo("Todo");
        todo.setId(1L);

        // when
        when(todoService.createTodo(createTodo)).thenReturn(todo.toDTO());

        // then
        mockMvc.perform(post("/api/v1/todos").contentType(MediaType.APPLICATION_JSON))  // no content -> throw error
            .andDo(print());
    }

    @Test
    public void createTodoControllerTest() throws Exception {
        // given
        Todo todo = new Todo("Todo");
        todo.setId(1L);

        // when
        when(todoService.createTodo(any(CreateTodoDTO.class))).thenReturn(todo.toDTO());

        // then
        mockMvc.perform(post("/api/v1/todos").content("{\"content\":\"Todo\"}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.content", is("Todo")))
            .andExpect(jsonPath("$.status", is("NOT_YET")))
            .andDo(print());
    }

    @Test
    public void linkTodoControllerTest() throws Exception {
        // given
        Todo childTodo1 = new Todo("Todo1");
        childTodo1.setId(1L);
        Todo childTodo2 = new Todo("Todo2");
        childTodo2.setId(2L);

        Todo parentTodo = new Todo("Todo3");
        parentTodo.setId(3L);
        parentTodo.addChildren(childTodo1);
        parentTodo.addChildren(childTodo2);

        // when
        when(todoService.setLink(3L, List.of(1L, 2L))).thenReturn(parentTodo.toDTO());

        // then
        mockMvc.perform(patch("/api/v1/todos/3/link").content("{\"childrenIds\":[1,2]}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.children.length()", is(2)))
            .andExpect(jsonPath("$.children[0]['id']", is(1)))
            .andExpect(jsonPath("$.children[1]['id']", is(2)))
            .andDo(print());
    }

    @Test
    public void updateTodoStatusTest() throws Exception {
        // given
        Todo todo = new Todo("Todo1");
        todo.setId(1L);
        todo.setStatus(TodoStatus.DONE);

        // when
        when(todoService.updateStatus(1L, TodoStatus.DONE)).thenReturn(todo.toDTO());

        // then
        mockMvc.perform(patch("/api/v1/todos/1/status").content("{\"status\":\"DONE\"}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", is("Todo1")))
            .andExpect(jsonPath("$.status", is("DONE")))
            .andDo(print());
    }
}