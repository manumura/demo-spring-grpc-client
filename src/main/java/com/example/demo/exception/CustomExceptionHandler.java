package com.example.demo.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<Object> handleException(StatusRuntimeException e) {
        log.error(e.getMessage(), e);
        if (Status.NOT_FOUND.getCode().equals(e.getStatus().getCode())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getStatus().getDescription());
        }
        if (Status.FAILED_PRECONDITION.getCode().equals(e.getStatus().getCode())) {
            return ResponseEntity.badRequest().body(e.getStatus().getDescription());
        }
        if (Status.UNAUTHENTICATED.getCode().equals(e.getStatus().getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getStatus().getDescription());
        }
        return ResponseEntity.internalServerError().build();
    }
}