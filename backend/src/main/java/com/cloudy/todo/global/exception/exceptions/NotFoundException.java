package com.cloudy.todo.global.exception.exceptions;

public class NotFoundException extends HttpException {
    public NotFoundException(String message, ErrorCode errorCode) {
        super(404, message, errorCode);
    }
}
