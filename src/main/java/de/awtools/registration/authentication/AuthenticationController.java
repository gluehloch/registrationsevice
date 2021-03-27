package de.awtools.registration.authentication;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.awtools.registration.HttpConst;
import de.awtools.registration.register.RegistrationValidationJson;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Start the authentication/login process.
     */
    @ApiOperation(value = "login", nickname = "login", response = RegistrationValidationJson.class, notes = "Authentication login.")
    @CrossOrigin
    @PostMapping(path = "/login", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public Token login(@ApiParam(name = "nickname", type = "String", required = true) @RequestParam String nickname, @RequestParam String password) {
        Token loginToken = authenticationService.login(nickname, password);
        return loginToken;
    }

    @ApiOperation(value = "logout", nickname = "logout", response = RegistrationValidationJson.class, notes = "Authentication logout.")
    @CrossOrigin
    @PostMapping(path = "/logout", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public void logout(@RequestBody LogoutJson logoutJson) {
        String tokenAsString = logoutJson.getToken();
        Token token = new Token(tokenAsString);
        authenticationService.logout(null);
    }

}
