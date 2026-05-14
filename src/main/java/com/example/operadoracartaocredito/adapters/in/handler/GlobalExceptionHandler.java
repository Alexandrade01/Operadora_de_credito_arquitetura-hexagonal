package com.example.operadoracartaocredito.adapters.in.handler;

import com.example.operadoracartaocredito.application.domain.exceptions.ClienteJaPossuiCartaoException;
import com.example.operadoracartaocredito.application.domain.exceptions.ClienteNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global para tratamento de exceções da API
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteJaPossuiCartaoException.class)
    public ResponseEntity<ErrorResponse> handleClienteJaPossuiCartao(
            ClienteJaPossuiCartaoException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNaoEncontrado(
            ClienteNaoEncontradoException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse response = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor: " + ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Classe de resposta padrão para erros
     */
    public record ErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp
    ) {}

    /**
     * Classe de resposta para erros de validação
     */
    public record ValidationErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp,
            Map<String, String> errors
    ) {}
}

