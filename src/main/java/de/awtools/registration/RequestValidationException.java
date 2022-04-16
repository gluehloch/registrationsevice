package de.awtools.registration;

import org.springframework.http.HttpStatus;

import de.awtools.registration.register.DefaultRegistrationValidation;

/**
 * REST Request Validation Exception. Probably a parameter problem and will
 * cause a {@link HttpStatus#BAD_REQUEST}.
 * 
 * @author Andre Winkler
 */
public class RequestValidationException extends RuntimeException {

    private static final long serialVersionUID = -8769788823200942352L;

    private final DefaultRegistrationValidation registrationValidation;

    public RequestValidationException(DefaultRegistrationValidation validation) {
        super();
        this.registrationValidation = validation;
    }

    public RequestValidationException(Exception cause,
            DefaultRegistrationValidation validation) {

        super(cause);
        this.registrationValidation = validation;
    }

    public DefaultRegistrationValidation getValidation() {
        return registrationValidation;
    }

}
