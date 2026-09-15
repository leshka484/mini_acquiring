package com.example.miniacquiring.core.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractHttpException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
