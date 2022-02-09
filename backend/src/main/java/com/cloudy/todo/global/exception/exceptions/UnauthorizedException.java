package com.cloudy.todo.global.exception.exceptions;

public class UnauthorizedException extends HttpException {
    public UnauthorizedException(String message, ErrorCode errorCode) {
        super(401, message, errorCode);
    }
}
