package com.cloudy.todo.todo.exception;

public class ParentIsDoneException extends RuntimeException {
    public ParentIsDoneException(String message) {
        super(message);
    }
}
