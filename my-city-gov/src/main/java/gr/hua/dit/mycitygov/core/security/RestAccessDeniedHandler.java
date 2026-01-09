package gr.hua.dit.mycitygov.core.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        String json = "{"
                + "\"timestamp\": \"" + Instant.now().toString() + "\","
                + "\"status\": 403,"
                + "\"error\": \"Forbidden\","
                + "\"message\": \"" + accessDeniedException.getMessage() + "\","
                + "\"path\": \"" + request.getRequestURI() + "\""
                + "}";

        response.getWriter().write(json);
    }
}
