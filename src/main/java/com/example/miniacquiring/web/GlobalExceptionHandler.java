package com.example.miniacquiring.web;

import com.example.miniacquiring.core.dto.ErrorResponse;
import com.example.miniacquiring.core.exception.AbstractHttpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AbstractHttpException.class)
    public ResponseEntity<ErrorResponse> handle(AbstractHttpException exception) {
        var response = new ErrorResponse(
                exception.getStatus().value(),
                exception.getMessage());
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

}
