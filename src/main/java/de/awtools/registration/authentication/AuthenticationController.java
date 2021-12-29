package de.awtools.registration.authentication;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.awtools.registration.HttpConst;
import de.awtools.registration.register.RegistrationValidationJson;
import de.awtools.registration.user.Password;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

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
    public ResponseEntity<Token> login(@ApiParam(name = "nickname", type = "String", required = true) @RequestParam String nickname, @RequestParam String password) {
        Optional<Token> loginToken = authenticationService.login(nickname, Password.of(password));
        
        // res.addHeader(HEADER_STRING, TOKEN_PREFIX + token.getContent());
        
        return loginToken.map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    /**
     * Start the authentication/login process.
     */
    @ApiOperation(value = "login", nickname = "login", response = RegistrationValidationJson.class, notes = "Authentication refresh.")
    @CrossOrigin
    @PostMapping(path = "/login", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public ResponseEntity<Token> refresh(@ApiParam(name = "nickname", type = "String", required = true) @RequestHeader(HEADER_STRING) String token, @RequestParam String nickname, @RequestParam String password) {
        return null; // Create a refresh token
    }    
    
    @ApiOperation(value = "logout", nickname = "logout", response = RegistrationValidationJson.class, notes = "Authentication logout.")
    @CrossOrigin
    @PostMapping(path = "/logout", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public void logout(@RequestBody LogoutJson logoutJson) {
        // TODO Token token = new Token( logoutJson.getToken());
        authenticationService.logout(null);
    }

}
