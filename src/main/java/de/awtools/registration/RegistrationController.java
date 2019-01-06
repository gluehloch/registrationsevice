package de.awtools.registration;

import java.time.LocalDateTime;
import java.util.Set;

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

    private static final String HEADER = "Content-type=application/json;charset=UTF-8";
    private static final String JSON_UTF_8 = "application/json; charset=utf-8";

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private RegistrationService registrationService;

    /**
     * Starts the registration process. The caller receives an email with an URL
     * to confirm his address.
     *
     * @return Web-Service reachable?
     */
    @ApiOperation(value = "ping", nickname = "ping", response = DateTimeJson.class)
    @CrossOrigin
    @GetMapping(path = "/ping", produces = JSON_UTF_8)
    public DateTimeJson ping() {
        DateTimeJson dateTimeJson = new DateTimeJson();
        // LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        dateTimeJson.setDateTime(LocalDateTime.now());
        return dateTimeJson;
    }

    @CrossOrigin
    @PostMapping(path = "/register", headers = {
            HEADER }, produces = JSON_UTF_8)
    public RegistrationValidationJson register(
            @Valid @RequestBody RegistrationJson registration) {

        RegistrationValidation validation = registrationService
                .registerNewUserAccount(registration.getNickname(),
                        registration.getEmail(),
                        registration.getPassword(),
                        registration.getName(),
                        registration.getFirstname(),
                        registration.getApplicationName());

        return new RegistrationValidationJson(validation);
    }

    @ApiOperation(value = "validate", nickname = "validate", response = RegistrationValidationJson.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid application name") })
    @CrossOrigin
    @PostMapping(path = "/validate", headers = {
            HEADER }, produces = JSON_UTF_8)
    public RegistrationValidationJson validate(
            @RequestBody RegistrationJson registration) {

        RegistrationValidation validation = registrationService.validate(
                registration.getNickname(),
                registration.getEmail(),
                registration.getApplicationName());

        Set<ValidationCode> httpStatus400 = Set.of(
                ValidationCode.ILLEGAL_ARGUMENTS,
                ValidationCode.UNKNOWN_APPLICATION);

        if (httpStatus400.contains(validation.getValidationCode())) {
            LOG.info("Invalid request parameters {}", validation);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return new RegistrationValidationJson(validation);
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

        return new RegistrationValidationJson(validation);
    }

}
