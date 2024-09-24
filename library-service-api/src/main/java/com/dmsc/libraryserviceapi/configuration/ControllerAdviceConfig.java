package com.dmsc.libraryserviceapi.configuration;

import com.dmsc.libraryserviceapi.exception.LibraryInvalidDataException;
import com.dmsc.openlibraryapi.exception.OpenLibraryClientSdkException;
import com.dmsc.openlibraryapi.exception.OpenLibraryServerSdkException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdviceConfig {

    private static final String MESSAGE = "message";

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LibraryInvalidDataException.class)
    public ResponseEntity<Map<String, String>> handleLibraryInvalidDataException(LibraryInvalidDataException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(errors, ex.getHttpStatusCode());
    }

    @ExceptionHandler(OpenLibraryClientSdkException.class)
    public ResponseEntity<Map<String, String>> handleSdkException(OpenLibraryClientSdkException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.valueOf(ex.getErrorCode()));
    }

    @ExceptionHandler(OpenLibraryServerSdkException.class)
    public ResponseEntity<Map<String, String>> handleSdkException(OpenLibraryServerSdkException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.valueOf(ex.getErrorCode()));
    }
}
