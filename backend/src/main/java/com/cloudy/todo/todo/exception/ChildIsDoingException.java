package com.cloudy.todo.todo.exception;

public class ChildIsDoingException extends RuntimeException {
    public ChildIsDoingException(String message) {
        super(message);
    }
}
