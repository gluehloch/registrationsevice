package de.awtools.registration.register;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<RegistrationValidationJson> register(
            /*@RequestHeader("api-key") String apiKey,*/
            @Valid @RequestBody RegistrationJson registration) {

        // validateApiKey(apiKey);
        DefaultRegistrationValidation validation = registrationService.registerNewAccount(registration);

        return toResponse(validation);
    }

    private boolean validateApiKey(String apiKey) {
    	// jwt token / validierung gegen den KeyStore
        // api-key in der Liste der aktzeptierten Keys?
        // Falls ja return true, falls nein return false!
        return false;
    }

    /**
     * Validates the registration data. Should be called before <code>/register</code>.
     *
     * @param  registration The registration data
     * @return              A copy of the registration data.
     */
    @ApiOperation(value = "validate", nickname = "validate", response = RegistrationValidationJson.class, notes = "Validates possible new account infos")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Validation failed. Bad request.") })
    @CrossOrigin
    @PostMapping(path = "/validate", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public ResponseEntity<RegistrationValidationJson> validate(@RequestBody RegistrationJson registration) {
        DefaultRegistrationValidation validation = registrationService.validate(
                registration.getNickname(),
                registration.getEmail(),
                registration.getApplicationName());

        return toResponse(validation);
    }

    /**
     * The user confirmed his account.
     *
     * @param  token The unique token of a new user.
     * @return       ...
     */
    @ApiOperation(value = "confirm", nickname = "confirm", response = RegistrationValidationJson.class, notes = "confirm account")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Registration not confirmed. Bad request.") })
    @CrossOrigin
    @PostMapping(value = "/confirm/{token}")
    public ResponseEntity<RegistrationValidationJson> confirm(@PathVariable String token) {
        DefaultRegistrationValidation validation = registrationService.confirmAccount(new Token(token));

        return toResponse(validation);
    }

    /**
     * Create user account.
     *
     * TODO Absichern!
     *
     * @param  registration The registration data.
     * @return              A copy of the registration data.
     */
    @ApiOperation(value = "create", nickname = "create", response = RegistrationValidationJson.class, notes = "Starts the registration process")
    @CrossOrigin
    @PostMapping(path = "/create", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public ResponseEntity<RegistrationValidationJson> create(@Valid @RequestBody RegistrationJson registration) {
        DefaultRegistrationValidation validation = registrationService.createAccount(registration);

        return toResponse(validation);
    }

    private ResponseEntity<RegistrationValidationJson> toResponse(Validation validation) {
        return ValidationResultMapper.<ResponseEntity<RegistrationValidationJson>>of(validation)
                .ifValid(RegistrationController::registrationIsValid)
                .orElse(RegistrationController::registrationFailed);
    }

    private static ResponseEntity<RegistrationValidationJson> registrationIsValid(Validation validation) {
        return ResponseEntity.ok(RegistrationValidationJson.of(validation));
    }

    private static ResponseEntity<RegistrationValidationJson> registrationFailed(Validation validation) {
        LOG.info("Registration failed: {}", validation);
        return ResponseEntity.badRequest().body(RegistrationValidationJson.of(validation));
    }

}
