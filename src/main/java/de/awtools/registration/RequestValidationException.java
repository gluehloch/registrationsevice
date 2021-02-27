package de.awtools.registration;

import de.awtools.registration.register.RegistrationValidation;
import org.springframework.http.HttpStatus;

/**
 * REST Request Validation Exception. Probably a parameter problem and will
 * cause a {@link HttpStatus#BAD_REQUEST}.
 * 
 * @author Andre Winkler
 */
public class RequestValidationException extends RuntimeException {

    private static final long serialVersionUID = -8769788823200942352L;

    private final RegistrationValidation registrationValidation;

    public RequestValidationException(RegistrationValidation validation) {
        super();
        this.registrationValidation = validation;
    }

    public RequestValidationException(Exception cause,
            RegistrationValidation validation) {

        super(cause);
        this.registrationValidation = validation;
    }

    public RegistrationValidation getValidation() {
        return registrationValidation;
    }

}
