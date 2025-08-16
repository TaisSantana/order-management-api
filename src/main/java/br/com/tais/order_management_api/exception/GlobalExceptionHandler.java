package br.com.tais.order_management_api.exception;

import br.com.tais.order_management_api.model.ErrorResponse;
import br.com.tais.order_management_api.model.enums.Roles;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.warn("Malformed JSON: {}", ex.getMessage());
        Throwable cause = ex.getCause();

        // Se for erro de enum inválido
        if (cause instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();

            if (targetType.isEnum() && targetType == Roles.class) {
                String invalidValue = invalidFormatException.getValue().toString();
                String allowedValues = Arrays.stream(Roles.values())
                        .map(Roles::getAuthority)
                        .collect(Collectors.joining(", "));

                String message = "Role inválida: '" + invalidValue + "'. Opções válidas: " + allowedValues;

                return buildError(
                        HttpStatus.BAD_REQUEST,
                        "Role inválida",
                        message,
                        request.getRequestURI()
                );
            }
        }

        return buildError(HttpStatus.BAD_REQUEST, "Malformed JSON request", ex.getMessage(), request.getRequestURI());

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos");

        logger.warn("Validation error: {}", message);
        return buildError(HttpStatus.BAD_REQUEST, "Erro de validação", message, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        logger.error("Erro de persistência", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", "Ocorreu um erro no Servidor", request.getRequestURI());
    }

    // Erro de validação em query params / path variables
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex, HttpServletRequest request) {
        String message = ex.getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Parâmetros inválidos");
        logger.warn("Bind error: {}", message);
        return buildError(HttpStatus.BAD_REQUEST, "Erro de parâmetros", message, request.getRequestURI());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Parâmetro ausente", ex.getParameterName() + " é obrigatório", request.getRequestURI());
    }

    // Tipo de parâmetro inválido (ex.: ID não numérico)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";
        String message = String.format("O valor '%s' não é válido para o parâmetro '%s'", value, ex.getName());
        return buildError(HttpStatus.BAD_REQUEST, "Tipo de parâmetro inválido", message, request.getRequestURI());
    }

    // Método HTTP não suportado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return buildError(HttpStatus.METHOD_NOT_ALLOWED, "Método não suportado", ex.getMessage(), request.getRequestURI());
    }

    // Erros de persistência genéricos
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorResponse> handlePersistence(PersistenceException ex, HttpServletRequest request) {
        logger.error("Erro de persistência", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Ocorreu um erro no servidor", request.getRequestURI());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, EntityExistsException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrity(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, "Violação de integridade", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return buildError(
                HttpStatus.NOT_FOUND,
                "Entidade não encontrada",
                ex.getMessage() != null ? ex.getMessage() : "O recurso solicitado não foi encontrado",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        return buildError(
                HttpStatus.NOT_FOUND,
                "Não encontrado",
                ex.getMessage() != null ? ex.getMessage() : "O recurso solicitado não foi encontrado",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {

        String message = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        logger.warn("Constraint violation: {}", message);
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                message ,
                request.getRequestURI()
        );
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            UsernameAlreadyExistsException.class,
            WeakPasswordException.class
    })
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(RuntimeException ex, HttpServletRequest request) {
        logger.warn("Business exception: {}", ex.getMessage());
        return buildError(HttpStatus.CONFLICT, "Conflito de regra de negócio", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex, HttpServletRequest request) {
        logger.warn("Not found: {}", ex.getMessage());
        return buildError(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String error, String message, String path) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                path
        );
        return ResponseEntity.status(status).body(body);
    }
}

