package com.cljtech.clinica.exception;

import com.cljtech.clinica.records.ErroResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensagem = "Registro viola uma regra de integridade.";

        Throwable causa = ex.getMostSpecificCause();

        if (causa != null && causa.getMessage() != null) {
            String detalhe = causa.getMessage().toLowerCase();

            if (detalhe.contains("uc_usuarioslogin_col")) {
                mensagem = "Login já está em uso.";
            }
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErroResponse(mensagem));
    }
}
