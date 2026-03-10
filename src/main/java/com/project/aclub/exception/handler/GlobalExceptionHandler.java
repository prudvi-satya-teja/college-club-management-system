package com.project.aclub.exception.handler;

import com.project.aclub.dto.ErrorResponse;
import com.project.aclub.exception.ConflictException;
import com.project.aclub.exception.FileUploadException;
import com.project.aclub.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                         WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "NOT_FOUND",
                request.getDescription(false).replace("uri=", ""));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage(), "Conflict",
                request.getDescription(false).replace("uri=", ""));
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException(FileUploadException ex, WebRequest request)  {
        return buildErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), "File Upload Exception",
                request.getDescription(false).replace("uri=", ""));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(int status, String message, String error,
                                                             String path) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setError(error);
        errorResponse.setPath(path);
        errorResponse.setMessage(message);
        errorResponse.setStatus(status);
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(status).body(errorResponse);
    }

}
