package de.awtools.registration;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.awtools.registration.RegistrationValidation.ValidationCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The registration controller.
 *
 * @author Andre Winkler
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private static final Logger LOG = LogManager.getLogger();

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    
    /**
     * Here starts the registration process.
     *
     * @param registration
     *            The registration data.
     * @return A copy of the registration data.
     */
    @ApiOperation(value = "register", nickname = "register", response = RegistrationValidationJson.class, notes = "Starts the registration process")
    @CrossOrigin
    @PostMapping(path = "/register", headers = {
            HttpConst.HEADER }, produces = HttpConst.JSON_UTF_8)
    public RegistrationValidationJson register(
            @Valid @RequestBody RegistrationJson registration) {

        RegistrationValidation validation = registrationService
                .registerNewUserAccount(registration.getNickname(),
                        registration.getEmail(),
                        registration.getPassword(),
                        registration.getName(),
                        registration.getFirstname(),
                        registration.getApplicationName(),
                        registration.isAcceptMail(),
                        registration.isAcceptCookie(),
                        registration.getSupplement());

        return RegistrationValidationJson.of(validation);
    }

    /**
     * Validates the registration data. Should be called before
     * <code>/register</code>.
     *
     * @param registration
     *            The registration data
     * @return A copy of the registration data.
     */
    @ApiOperation(value = "validate", nickname = "validate", response = RegistrationValidationJson.class, notes = "Validates possible new account infos")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid application name") })
    @CrossOrigin
    @PostMapping(path = "/validate", headers = {
            HttpConst.HEADER }, produces = HttpConst.JSON_UTF_8)
    public RegistrationValidationJson validate(
            @RequestBody RegistrationJson registration) {

        RegistrationValidation validation = registrationService.validate(
                registration.getNickname(),
                registration.getEmail(),
                registration.getApplicationName());

        toResponseStatusException(validation);

        return RegistrationValidationJson.of(validation);
    }

    /**
     * The user confirmed his account.
     *
     * @param token
     *            The unique token of a new user.
     * @return ...
     */
    @CrossOrigin
    @PostMapping(value = "/confirm/{token}")
    public RegistrationValidationJson confirm(@PathVariable String token) {
        RegistrationValidation validation = registrationService
                .confirmAccount(new Token(token));

        toResponseStatusException(validation);

        return RegistrationValidationJson.of(validation);
    }

    /**
     * Throws an exception:
     * <ul>
     * <li>The application is unknown.</li>
     * </ul>
     * 
     * @param rv
     *            The validation result.
     */
    private void toResponseStatusException(RegistrationValidation rv) {
        // Http Status Code 400: Bad request
        // Set<ValidationCode> httpStatus400 =
        // Set.of(ValidationCode.UNKNOWN_APPLICATION);

        if (rv.getValidationCodes()
                .contains(ValidationCode.UNKNOWN_APPLICATION)) {
            LOG.error("Find an unknown application.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid application parameter.");
        }
    }

}
