package de.awtools.registration.config;

import de.awtools.registration.RegistrationValidation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.awtools.registration.RequestValidationException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

@ControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { RequestValidationException.class })
    protected ResponseEntity<Object> handleConflict(Exception ex,
            WebRequest request) {

        JsonObjectBuilder json = Json.createObjectBuilder();

        if (ex instanceof RequestValidationException) {
            RequestValidationException rvex = (RequestValidationException) ex;

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (RegistrationValidation.ValidationCode vc : rvex.getValidation()
                    .getValidationCodes()) {
                arrayBuilder.add(vc.name());
            }

            json.add("message", arrayBuilder.build());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return handleExceptionInternal(ex, json.build(), headers,
                HttpStatus.BAD_REQUEST, request);
    }

}
