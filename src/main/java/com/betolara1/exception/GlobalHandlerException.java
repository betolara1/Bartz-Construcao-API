package com.betolara1.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalHandlerException {
    // TRATAMENTO DE RECURSO NÃO ENCONTRADO (404)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardErrorDTO> handleRecursoNaoEncontrado(NotFoundException ex,
            HttpServletRequest request) {

        StandardErrorDTO erro = new StandardErrorDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Não Encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorDTO> handleIllegalArgument(IllegalArgumentException ex,
        HttpServletRequest request) {
    StandardErrorDTO erro = new StandardErrorDTO(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Requisição Inválida",
            ex.getMessage(),
            request.getRequestURI()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsMap(errors));
    }

    private Map<String, List<String>> errorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
  }
}
