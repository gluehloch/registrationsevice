package de.awtools.registration.config;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.awtools.registration.RequestValidationException;
import de.awtools.registration.register.DefaultRegistrationValidation;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

@ControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { RequestValidationException.class })
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        JsonObjectBuilder json = Json.createObjectBuilder();

        if (ex instanceof RequestValidationException) {
            RequestValidationException rvex = (RequestValidationException) ex;

            json.add("nickname", rvex.getValidation().getNickname());
            json.add("applicationName", rvex.getValidation().getApplicationName());

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (DefaultRegistrationValidation.ValidationCode vc : rvex.getValidation()
                    .getValidationCodes()) {
                arrayBuilder.add(vc.name());
            }

            json.add("validationCode", arrayBuilder.build());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(ex, jsonToString(json), headers,
                HttpStatus.BAD_REQUEST, request);
    }

    private String jsonToString(JsonObjectBuilder jsonBuilder) {
        String jsonString;
        try(Writer writer = new StringWriter()) {
            Json.createWriter(writer).write(jsonBuilder.build());
            jsonString = writer.toString();
        } catch (IOException ioex) {
            jsonString = ioex.getLocalizedMessage();
        }
        return jsonString;
    }

}
