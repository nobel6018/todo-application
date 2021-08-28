package com.cloudy.todo.todo.dto.request;

import com.cloudy.todo.todo.domain.TodoStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateStatusTodoDTO {

    private TodoStatus status;
}
