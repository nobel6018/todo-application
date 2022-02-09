package com.cloudy.todo.global.dto;

import com.cloudy.todo.global.exception.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResult {
    private final int statusCode;
    private final String message;
    private final ErrorCode error;

    public ErrorResult(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = null;
    }
}
