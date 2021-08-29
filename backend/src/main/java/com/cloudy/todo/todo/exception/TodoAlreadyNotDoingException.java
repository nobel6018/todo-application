package com.cloudy.todo.todo.exception;

public class TodoAlreadyNotDoingException extends RuntimeException {
    public TodoAlreadyNotDoingException(String message) {
        super(message);
    }
}
