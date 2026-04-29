package com.agenda.exeption;

import com.agenda.dtos.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExeptionHandler  {
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> EmailNaoEncontrado(IllegalStateException ex) {
        return ResponseEntity.status(409).body(
                new ErrorResponse(409, ex.getMessage(), LocalDateTime.now())
        );
}
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> BuscaInvalida(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(
                new ErrorResponse(400, ex.getMessage(), LocalDateTime.now())
        );

    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> ContatoNaoEncontrado(RuntimeException ex) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(404, ex.getMessage(), LocalDateTime.now())
        );


    }







}
