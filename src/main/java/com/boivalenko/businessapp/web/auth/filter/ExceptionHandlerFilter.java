package com.boivalenko.businessapp.web.auth.filter;

import com.boivalenko.businessapp.web.auth.obj.JsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Wrapper Exception Filter. Filter packt Exceptions in JSON Format,
// dass der Client diese Exceptions versteht
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            JsonException exception = new JsonException(e.getClass().getSimpleName());

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
