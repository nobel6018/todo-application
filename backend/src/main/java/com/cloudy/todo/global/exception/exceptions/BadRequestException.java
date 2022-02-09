package com.cloudy.todo.global.exception.exceptions;

public class BadRequestException extends HttpException {
    public BadRequestException(String message, ErrorCode errorCode) {
        super(400, message, errorCode);
    }
}
