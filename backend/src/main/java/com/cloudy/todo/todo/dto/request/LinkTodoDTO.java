package com.cloudy.todo.todo.dto.request;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LinkTodoDTO {

    @NotNull
    private List<Long> childrenIds;
}
