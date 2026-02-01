package com.hotelBooking.controller;

import com.hotelBooking.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 — Entity not found
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(RuntimeException ex) {
        String message = ex.getMessage();
        // Detect "not found" messages
        if (message != null && message.toLowerCase().contains("not found")) {
            ErrorResponseDto errorDto = ErrorResponseDto.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(message)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
        }
        // Otherwise treat as 500
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error: " + message)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    // 400 — Client errors (bad input, illegal argument)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(IllegalArgumentException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    // 400 — Validation errors
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(jakarta.validation.ConstraintViolationException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation error: " + ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    // 500 — Catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred: " + ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
