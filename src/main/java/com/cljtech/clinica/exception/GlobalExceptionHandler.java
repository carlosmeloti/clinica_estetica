package com.cljtech.clinica.exception;

import com.cljtech.clinica.model.records.ErroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final Pattern CONSTRAINT_PATTERN = Pattern.compile("constraint \\[?\"?([^\"\\]\\s]+)\"?\\]?");

    private final MessageSource messageSource;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarDataIntegrityViolation(DataIntegrityViolationException ex) {

        Locale locale = LocaleContextHolder.getLocale();
        String detalhe = ex.getMostSpecificCause().getMessage();

        String mensagemFinal = extrairConstraint(detalhe)
                .map(constraint -> buscarMensagem("db.constraint." + constraint, locale))
                .orElseGet(() -> buscarMensagem("db.error.generic", locale));

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErroResponse(mensagemFinal));
    }

    private java.util.Optional<String> extrairConstraint(String detalhe) {
        if (detalhe == null) {
            return java.util.Optional.empty();
        }

        Matcher matcher = CONSTRAINT_PATTERN.matcher(detalhe);

        if (matcher.find()) {
            return java.util.Optional.of(matcher.group(1));
        }

        return java.util.Optional.empty();
    }

    private String buscarMensagem(String chave, Locale locale) {
        try {
            return messageSource.getMessage(chave, null, locale);
        } catch (NoSuchMessageException ex) {
            return messageSource.getMessage("db.error.generic", null, locale);
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErroResponse> tratarAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErroResponse("Login ou senha inválidos"));
    }
}
