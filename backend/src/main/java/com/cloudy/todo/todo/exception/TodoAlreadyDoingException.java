package com.cloudy.todo.todo.exception;

public class TodoAlreadyDoingException extends RuntimeException {
    public TodoAlreadyDoingException(String message) {
        super(message);
    }
}
