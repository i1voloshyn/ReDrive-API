package dev.ivanvoloshyn.redriveapi.exception;

import dev.ivanvoloshyn.redriveapi.exception.model.ApiException;
import dev.ivanvoloshyn.redriveapi.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e,
                                                                   HttpServletRequest request) {
        String path = request.getRequestURI();
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Objects.requireNonNull(error.getDefaultMessage(), "Invalid value"),
                        (first, second) -> second
                ));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, path, "Validation failed", errors);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e, HttpServletRequest request) {
        return buildErrorResponse(e.getHttpStatus(), e.getMessage(), request);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Throwable ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus httpStatus, String message, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ResponseEntity.status(httpStatus).body(new ErrorResponse(httpStatus.value(), path, message));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String path, String message, Map<String, String> errors) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), path, message, errors));
    }

}
