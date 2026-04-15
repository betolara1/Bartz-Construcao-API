package com.betolara1.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.betolara1.dto.StandardErrorDTO;

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
}
