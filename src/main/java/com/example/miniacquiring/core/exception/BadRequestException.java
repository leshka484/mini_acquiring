package com.example.miniacquiring.core.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractHttpException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
