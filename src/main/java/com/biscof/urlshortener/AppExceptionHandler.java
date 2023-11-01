package com.biscof.urlshortener;

import com.biscof.urlshortener.exception.LinkNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({ LinkNotFoundException.class })
    public ResponseEntity<Object> handleLinkNotFoundException(LinkNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}