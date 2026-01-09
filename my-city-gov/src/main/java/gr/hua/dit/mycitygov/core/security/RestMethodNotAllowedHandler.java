package gr.hua.dit.mycitygov.core.security;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestMethodNotAllowedHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Map<String, Object> handleMethodNotAllowed(HttpServletRequest request,
            HttpRequestMethodNotSupportedException e) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", "405",
                "error", "Method not allowed",
                "method", request.getMethod(),
                "message", "Use " + e.getSupportedHttpMethods() + " instead of " + e.getMethod(),
                "path", request.getRequestURI());
    }
}
