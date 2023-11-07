package com.biscof.urlshortener.exception.handler;

import com.biscof.urlshortener.exception.ErrorResponse;
import com.biscof.urlshortener.exception.LinkNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;


@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLinkNotFoundException(
            LinkNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ErrorResponse> errorList = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        error.getDefaultMessage(),
                        request.getRequestURI())
                )
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
    }

}
