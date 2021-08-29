package com.cloudy.todo.todo.exception;

public class TodoAlreadyDoneException extends RuntimeException {
    public TodoAlreadyDoneException(String message) {
        super(message);
    }
}
