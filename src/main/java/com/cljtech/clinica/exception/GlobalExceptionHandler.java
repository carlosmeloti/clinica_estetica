package com.cljtech.clinica.exception;


import com.cljtech.clinica.model.records.ErroResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private static final Pattern CONSTRAINT_PATTERN = Pattern.compile("constraint \\[?\"?([^\"\\]\\s]+)\"?\\]?");

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Erro de validação nos argumentos da requisição: {}", ex.getMessage());
        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatarErroDeCampo)
                .findFirst()
                .orElse("Dados inválidos.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(mensagem));
    }

    private String formatarErroDeCampo(FieldError erro) {
        return erro.getDefaultMessage() != null
                ? erro.getDefaultMessage()
                : "Campo inválido: " + erro.getField();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> tratarConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Erro de validação de constraint: {}", ex.getMessage());
        String mensagem = ex.getConstraintViolations()
                .stream()
                .map(violacao -> violacao.getMessage())
                .findFirst()
                .orElse("Dados inválidos.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(mensagem));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> tratarIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponse> tratarIllegalStateException(IllegalStateException ex) {
        log.warn("Estado inválido: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(ex.getMessage()));
    }
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(ex.getMessage()));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> tratarRegraNegocioException(RegraNegocioException ex) {
        log.warn("Regra de negócio violada: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(ex.getMessage()));
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponse> tratarConflitoException(ConflitoException ex) {
        log.warn("Conflito na requisição: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErroResponse(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> tratarRuntimeException(RuntimeException ex) {
        log.error("Erro interno inesperado", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse("Ocorreu um erro interno inesperado."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade no banco de dados: {}", ex.getMostSpecificCause().getMessage());

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
        log.warn("Falha de autenticação: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErroResponse("Login ou senha inválidos"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarException(Exception ex) {
        log.error("Erro inesperado não tratado", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse("Ocorreu um erro interno inesperado."));
    }
}
