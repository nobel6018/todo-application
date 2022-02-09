package com.cloudy.todo.global.exception.exceptions;

import lombok.Getter;

@Getter
public class HttpException extends RuntimeException {
    private final int statusCode;
    private final String customMessage;
    private final ErrorCode errorCode;

    public HttpException(int statusCode, String customMessage) {
        this.statusCode = statusCode;
        this.customMessage = customMessage;
        this.errorCode = null;
    }

    public HttpException(int statusCode, String customMessage, ErrorCode errorCode) {
        this.statusCode = statusCode;
        this.customMessage = customMessage;
        this.errorCode = errorCode;
    }
}
