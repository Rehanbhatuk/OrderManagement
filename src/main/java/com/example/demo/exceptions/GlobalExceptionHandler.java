package com.example.demo.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RestParameterInvalidException.class)
    public ResponseEntity<?> handleRestParameterException(RestParameterInvalidException e) {


        log.error("An unexpected error occurred: ", e);

        return ResponseEntity.status(500).body(e.getErrors());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)


    public ResponseEntity<?> handleMethodArgumentException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();


        for (org.springframework.validation.FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.warn("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }


}