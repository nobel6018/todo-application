package com.cloudy.todo.todo.dto;

import com.cloudy.todo.todo.domain.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TodoDTO {

    private final Long id;
    private final String content;
    private final List<TodoDTO> children;
    private final TodoStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
