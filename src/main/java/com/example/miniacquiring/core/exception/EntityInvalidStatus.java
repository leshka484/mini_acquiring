package com.example.miniacquiring.core.exception;

import org.springframework.http.HttpStatus;

public class EntityInvalidStatus extends AbstractHttpException {

    public EntityInvalidStatus(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
