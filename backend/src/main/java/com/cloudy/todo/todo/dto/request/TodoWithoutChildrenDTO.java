package com.cloudy.todo.todo.dto.request;

import com.cloudy.todo.todo.domain.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TodoWithoutChildrenDTO {

    private final Long id;
    private final String content;
    private final TodoStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
