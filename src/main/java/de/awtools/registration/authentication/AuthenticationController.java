package de.awtools.registration.authentication;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.*;

import de.awtools.registration.HttpConst;
import de.awtools.registration.register.RegistrationValidationJson;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    /**
     * Start the authentication/login process.
     */
    @ApiOperation(value = "login", nickname = "login", response = RegistrationValidationJson.class, notes = "Authentication login.")
    @CrossOrigin
    @PostMapping(path = "/login", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public void login(@ApiParam(name = "nickname", type = "String", required = true) @RequestParam String nickname, @RequestParam String password) {
//        if (loginService.login(nickname, password)) {
//            UserDetails userDetails = loginService.loadUserByUsername(nickname);
//            return loginService.token(userDetails);
//        } else {
//            return null;
//        }

    }

    @ApiOperation(value = "logout", nickname = "logout", response = RegistrationValidationJson.class, notes = "Authentication logout.")
    @CrossOrigin
    @PostMapping(path = "/logout", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public void logout(@RequestBody LogoutJson logoutJson) {

    }

}
