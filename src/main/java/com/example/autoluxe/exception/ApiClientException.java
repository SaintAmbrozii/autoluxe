package com.example.autoluxe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ApiClientException extends HttpClientErrorException {

    public ApiClientException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
