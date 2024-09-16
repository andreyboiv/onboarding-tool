package com.boivalenko.businessapp.teamtasksplanning.web.auth.filter;

import com.boivalenko.businessapp.teamtasksplanning.web.auth.obj.JsonExcept;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// Wrapper Exception Filter. Filter packt Exceptions in JSON Format,
// dass der Client diese Exceptions versteht
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            JsonExcept exception = new JsonExcept(e.getClass().getSimpleName());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(convertOjectToJson(exception));
        }
    }

    private String convertOjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
