package com.example.autoluxe.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //  срабатывает на выбрасываемое сервисом исключение

    @ExceptionHandler(AutoluxeException.class)
    public ProblemDetail handleMenuServiceException(AutoluxeException ex, WebRequest request) {
        log.error("Intercepted MenuServiceException. Message: {}. Status: {}", ex.getMessage(), ex.getStatus());
        return createProblemDetail(ex.getMessage(), ex.getStatus(), request);
    }

    /**
     *  срабатывает, когда не удается прочесть тело входящего запроса.
     */

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        log.error("Intercepted HttpMessageNotReadableException. Message: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(createProblemDetail(ex.getMessage(), HttpStatus.BAD_REQUEST, request));
    }

    /**
     * срабатывает, когда присутствуют ошибки валидации в теле запроса.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        var pd = ex.getBody();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getGlobalErrors().forEach(e -> {
            errors.put(e.getObjectName(), e.getDefaultMessage());
        });
        ex.getBindingResult().getFieldErrors().forEach(e -> {
            errors.put(e.getField(), e.getDefaultMessage());
        });
        log.error("Intercepted MethodArgumentNotValidException. Errors: {}", errors);
        pd.setProperty("invalid_params", errors);
        pd.setStatus(HttpStatus.BAD_REQUEST);
        pd.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));
        return handleExceptionInternal(ex, pd, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * срабатывает, когда в методах restController-а есть параметры, помеченные @Valid,
     * например: @PathVariable("id") @Positive(message = "id должен быть > 0.") @Valid Long id,
     * но проверку они не проходят.
     */
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                            HttpHeaders headers, HttpStatusCode status,
                                                                            WebRequest request) {
        var pd = ex.getBody();
        Map<String, String> errors = new HashMap<>();
        ex.getAllValidationResults().forEach(result -> {
            result.getResolvableErrors().forEach(e -> {
                errors.put(result.getMethodParameter().getParameterName(), e.getDefaultMessage());
            });
        });
        log.error("Intercepted HandlerMethodValidationException. Errors: {}", errors);
        pd.setProperty("invalid_params", errors);
        pd.setStatus(HttpStatus.BAD_REQUEST);
        pd.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));
        return handleExceptionInternal(ex, pd, headers, HttpStatus.BAD_REQUEST, request);
    }


    private static ProblemDetail createProblemDetail(String message, HttpStatus status, WebRequest request) {
        var pd = ProblemDetail.forStatusAndDetail(status, message);
        pd.setProperty("timestamp", Instant.now());
        pd.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));
        return pd;
    }

}
