package com.cloudy.todo.todo.dto.response;

import com.cloudy.todo.todo.domain.TodoStatus;
import com.cloudy.todo.todo.dto.request.TodoWithoutChildrenDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TodoDTO {

    private final Long id;
    private final String content;
    private final List<TodoWithoutChildrenDTO> children;
    private final TodoStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
