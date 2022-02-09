package com.cloudy.todo.global.exception.exceptions;

public class ForbiddenException extends HttpException {
    public ForbiddenException(String message, ErrorCode errorCode) {
        super(403, message, errorCode);
    }
}
