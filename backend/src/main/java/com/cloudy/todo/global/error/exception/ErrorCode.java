package com.cloudy.todo.global.error.exception;

public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", "Method Not Allow"),
    ENTITY_NOT_FOUND(404, "ENTITY_NOT_FOUND", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "Internal Server Error"),
    ;

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
