package com.cloudy.todo.global.exception;

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
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTooManySpeakerPostRequest(IllegalArgumentException e) {
        log.info("[IllegalArgumentException] e", e);

        ErrorResult errorResult = new ErrorResult("IllegalArgumentException", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
