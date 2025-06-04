package com.example.demo.exceptionhandler;

import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.CategoryNameAlreadyExistsException;
import com.example.demo.exception.EmailAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Para entidades no encontradas (e.g., Categoria, Gasto, Usuario por ID)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Para validaciones de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                errors.toString(), // Se puede mejorar la presentación de los errores de campo
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Para EmailAlreadyExistsException
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflicto de datos",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Para CategoryNameAlreadyExistsException
    @ExceptionHandler(CategoryNameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCategoryNameAlreadyExistsException(CategoryNameAlreadyExistsException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflicto de datos",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Para nuestra AccessDeniedException personalizada (usada en servicios)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Acceso denegado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Para AccessDeniedException de Spring Security (usada por @PreAuthorize)
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleSpringSecurityAccessDeniedException(org.springframework.security.access.AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Acceso denegado",
                "No tiene permisos para acceder a este recurso.", // Mensaje genérico
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Para AuthenticationException de Spring Security (e.g., BadCredentialsException)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "No autorizado",
                "Credenciales inválidas o token no válido.", // Mensaje genérico
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Handler genérico para cualquier otra excepción no capturada específicamente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, HttpServletRequest request) {
        // Loggear la excepción es importante aquí para diagnóstico
        // logger.error("Excepción no esperada: ", ex);
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                "Ocurrió un error inesperado. Por favor, intente más tarde.", // Mensaje genérico para el usuario
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 