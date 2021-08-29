package com.cloudy.todo.todo.exception;

import com.cloudy.todo.global.dto.ErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class TodoExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTooManySpeakerPostRequest(TodoNotFoundException e) {
        log.info("[TodoNotFoundException] e", e);

        ErrorResult errorResult = new ErrorResult("TodoNotFoundException", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTooManySpeakerPostRequest(ParentIsDoneException e) {
        log.info("[ParentIsDoneException] e", e);

        ErrorResult errorResult = new ErrorResult("ParentIsDoneException", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTooManySpeakerPostRequest(ChildIsDoingException e) {
        log.info("[ChildIsDoingException] e", e);

        ErrorResult errorResult = new ErrorResult("ChildIsDoingException", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTooManySpeakerPostRequest(TodoAlreadyDoneException e) {
        log.info("[TodoAlreadyDoneException] e", e);

        ErrorResult errorResult = new ErrorResult("TodoAlreadyDoneException", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTooManySpeakerPostRequest(TodoAlreadyNotDoingException e) {
        log.info("[TodoAlreadyNotDoingException] e", e);

        ErrorResult errorResult = new ErrorResult("TodoAlreadyNotDoingException", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}