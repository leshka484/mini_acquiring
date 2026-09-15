package com.example.miniacquiring.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AbstractHttpException extends RuntimeException {

    private final HttpStatus status;

    protected AbstractHttpException(
            HttpStatus status,
            String message
    ) {
        super(message);
        this.status = status;
    }

}
