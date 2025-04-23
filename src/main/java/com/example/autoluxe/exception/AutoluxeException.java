package com.example.autoluxe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AutoluxeException extends RuntimeException{

    private final HttpStatus status;

    public AutoluxeException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }
}
