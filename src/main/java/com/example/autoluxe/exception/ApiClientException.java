package com.example.autoluxe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class ApiClientException extends RuntimeException {

    private final HttpStatus status;

    public ApiClientException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
