package de.awtools.registration.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.awtools.registration.RequestValidationException;

@ControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { RequestValidationException.class })
    protected ResponseEntity<Object> handleConflict(Exception ex,
            WebRequest request) {

        String bodyOfResponse = "Unknown";
        if (ex instanceof RequestValidationException) {
            RequestValidationException rvex = (RequestValidationException) ex;
            bodyOfResponse = new StringBuilder().append('{')
                    .append("\"message\": \"")
                    .append(rvex.getValidation().getValidationCode().toString())
                    .append("\"").append('}').toString();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return handleExceptionInternal(ex, bodyOfResponse,
                headers, HttpStatus.BAD_REQUEST, request);
    }

}
