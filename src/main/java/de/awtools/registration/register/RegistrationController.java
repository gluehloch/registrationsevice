package de.awtools.registration.register;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.awtools.registration.HttpConst;
import de.awtools.registration.Token;
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

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationController.class);

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Here starts the registration process.
     *
     * @param  registration The registration data.
     * @return              A copy of the registration data.
     */
    @ApiOperation(value = "register", nickname = "register", response = RegistrationValidationJson.class, notes = "Starts the registration process")
    @CrossOrigin
    @PostMapping(path = "/register", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public ResponseEntity<RegistrationValidationJson> register(@Valid @RequestBody RegistrationJson registration) {
        DefaultRegistrationValidation validation = registrationService
                .registerNewAccount(registration.getNickname(),
                        registration.getEmail(),
                        registration.getPassword(),
                        registration.getName(),
                        registration.getFirstname(),
                        registration.getApplicationName(),
                        registration.isAcceptMail(),
                        registration.isAcceptCookie(),
                        registration.getSupplement());

        return ResponseEntity.ok(RegistrationValidationJson.of(validation));
    }

    /**
     * Validates the registration data. Should be called before <code>/register</code>.
     *
     * @param  registration The registration data
     * @return              A copy of the registration data.
     */
    @ApiOperation(value = "validate", nickname = "validate", response = RegistrationValidationJson.class, notes = "Validates possible new account infos")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid application name") })
    @CrossOrigin
    @PostMapping(path = "/validate", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public ResponseEntity<RegistrationValidationJson> validate(@RequestBody RegistrationJson registration) {
        DefaultRegistrationValidation validation = registrationService.validate(
                registration.getNickname(),
                registration.getEmail(),
                registration.getApplicationName());

        // toResponseStatusException(validation);

        return ResponseEntity.ok(RegistrationValidationJson.of(validation));
    }

    /**
     * The user confirmed his account.
     *
     * @param  token The unique token of a new user.
     * @return       ...
     */
    @CrossOrigin
    @PostMapping(value = "/confirm/{token}")
    public ResponseEntity<RegistrationValidationJson> confirm(@PathVariable String token) {
        DefaultRegistrationValidation validation = registrationService.confirmAccount(new Token(token));

        return ValidationResultMapper.<ResponseEntity<RegistrationValidationJson>>of(validation)
                        .ifValid(_v -> ResponseEntity.ok(RegistrationValidationJson.of(_v)))
                        .orElse(_v -> ResponseEntity.badRequest().build());
    }

    private void registrationFailed(Validation validation) {
        LOG.info("Registration failed: {}", validation);
        ResponseEntity.badRequest();
    }

}
