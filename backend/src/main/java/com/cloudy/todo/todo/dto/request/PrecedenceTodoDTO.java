package com.cloudy.todo.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrecedenceTodoDTO {

    @NotNull
    private List<Long> precedenceIds;
}
