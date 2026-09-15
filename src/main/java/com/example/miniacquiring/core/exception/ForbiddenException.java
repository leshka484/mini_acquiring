package com.example.miniacquiring.core.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AbstractHttpException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

}
