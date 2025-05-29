package com.easyfarm.easyfarm.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException ex, Model model) {
        log.error("Resource not found: {}", ex.getMessage());
        model.addAttribute("errorMessage", "The requested resource was not found");
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        log.error("Access denied: {}", ex.getMessage());
        model.addAttribute("errorMessage", "You don't have permission to access this resource");
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllUncaughtException(Exception ex, Model model, HttpServletRequest request) {
        log.error("Unknown error occurred: {} at URI: {}", ex.getMessage(), request.getRequestURI(), ex);

        String errorMessage = "An unexpected error occurred";
        if (!isProd()) {
            errorMessage += ": " + ex.getMessage();
        }

        model.addAttribute("errorMessage", errorMessage);
        return "error/500";
    }

    private boolean isProd() {
        String profile = System.getProperty("spring.profiles.active");
        return profile != null && profile.contains("prod");
    }
}
