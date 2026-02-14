package com.bankapp.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                    "error", ex.getMessage(),
                    "status", 400
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleOther(RuntimeException ex) {
        return ResponseEntity
                .status(500)
                .body(Map.of(
                    "error", ex.getMessage(),
                    "status", 500
                ));
    }
}
