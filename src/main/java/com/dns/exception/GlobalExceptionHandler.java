package com.dns.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<?> handleFileStorage(FileStorageException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return buildErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, "File too large! Please upload smaller images.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid parameter: " + ex.getName());
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<?> handleGeneric(Exception ex) {
    // return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected
    // error: " + ex.getMessage());
    // }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<?> handleUnauthorizedAction(UnauthorizedActionException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
