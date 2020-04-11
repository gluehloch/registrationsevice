package de.awtools.registration;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.awtools.registration.RegistrationValidation.ValidationCode;
import de.awtools.registration.time.DateTimeJson;
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

    @Autowired
    private RegistrationService registrationService;

    /**
     * Shows the current version of this API.
     *
     * @return A version String.
     */
    @ApiOperation(value = "version", nickname = "version", response = String.class)
    @CrossOrigin
    @GetMapping(path = "version", produces = HttpConst.JSON_UTF_8)
    public VersionJson versionInfo() {
        return VersionJson.of();
    }

    /**
     * Starts the registration process. The caller receives an email with an URL
     * to confirm his address.
     *
     * @return Web-Service reachable?
     */
    @ApiOperation(value = "ping", nickname = "ping", response = DateTimeJson.class, notes = "Ping this service. Is it reachable?")
    @CrossOrigin
    @GetMapping(path = "/ping", produces = HttpConst.JSON_UTF_8)
    public DateTimeJson ping() {
        DateTimeJson dateTimeJson = new DateTimeJson();
        // LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        dateTimeJson.setDateTime(LocalDateTime.now());
        return dateTimeJson;
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
