package com.agenda.app.adapters.exeption;

import com.agenda.app.adapters.input.dtos.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExeptionHandler  {
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> IllegalState(IllegalStateException ex) {
        log.error("Exceção de campo inválido acionado: " + ex.getMessage());
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, ex.getMessage(), LocalDateTime.now())
        );
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> IllegalArgument(IllegalArgumentException ex){
        log.error("Exceção de argumento inválido acionado: " + ex.getMessage());
        return ResponseEntity.status(409).body(
                new ErrorResponse(409, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> ArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("Exceção de campo do request inválido acionado: " + ex.getMessage());
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, ex.getMessage(), LocalDateTime.now())
        );
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> Runtime(RuntimeException ex) {
        log.error("Exceção de dado não encontrado acionado: " + ex.getMessage());
        return ResponseEntity.status(404).body(
                new ErrorResponse(404, ex.getMessage(), LocalDateTime.now())
        );

    }

}
