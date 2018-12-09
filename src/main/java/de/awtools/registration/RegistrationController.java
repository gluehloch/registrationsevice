package de.awtools.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The registration controller.
 *
 * @author Andre Winkler
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    /**
     * Starts the registration process. The caller receives an email with
     * an URL to confirm his address.
     *
     * @param registration Registration data as JSON.
     * @return ...
     */
    @CrossOrigin
    @PostMapping(value = "/register", headers = {
            "Content-type=application/json" })
    public String register(RegistrationJson registration) {

        registrationService.registerAccount(registration.getUsername(),
                registration.getEmail(), registration.getPassword());
        return "TODO";
    }

    /**
     * The user confirmed his account.
     *
     * @param token The unique token of a new user.
     * @return ...
     */
    @CrossOrigin
    @PostMapping(value = "/confirm/{token}")
    public String confirm(@PathVariable String token) {
        registrationService.confirmAccount(token);
        
        return "TODO";
    }
    
    /*
     * @CrossOrigin
     * 
     * @RequestMapping(value = "/season/list", method = RequestMethod.GET)
     * public List<String> findAllSeason(HttpServletResponse response) { //
     * return betofficeBasicJsonService.findAllSeason(); return null }
     * 
     * @CrossOrigin
     * 
     * @RequestMapping(value = "/login", method = RequestMethod.POST, headers =
     * { "Content-type=application/json" }) public SecurityTokenJson login(
     * 
     * @RequestBody AuthenticationForm authenticationForm,
     * 
     * @RequestHeader(BetofficeHttpConsts.HTTP_HEADER_USER_AGENT) String
     * userAgent, HttpServletRequest request) {
     * 
     * SecurityTokenJson securityToken = betofficeBasicJsonService.login(
     * authenticationForm.getNickname(), authenticationForm.getPassword(),
     * request.getSession().getId(), request.getRemoteAddr(), userAgent);
     * 
     * HttpSession session = request.getSession();
     * session.setAttribute(SecurityToken.class.getName(), securityToken);
     * 
     * return securityToken; }
     * 
     * @CrossOrigin
     * 
     * @RequestMapping(value = "/logout", method = RequestMethod.POST, headers =
     * { "Content-type=application/json" }) public SecurityTokenJson
     * logout(@RequestBody LogoutFormData logoutFormData, HttpServletRequest
     * request) {
     * 
     * SecurityTokenJson securityTokenJson = betofficeBasicJsonService.logout(
     * logoutFormData.getNickname(), logoutFormData.getToken());
     * 
     * HttpSession session = request.getSession();
     * session.removeAttribute(SecurityToken.class.getName());
     * 
     * return securityTokenJson; }
     */
}
