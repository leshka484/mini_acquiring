package com.example.miniacquiring.core.exception;

import org.springframework.http.HttpStatus;

public class EntityNotVerifiedException extends AbstractHttpException {

    public EntityNotVerifiedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
