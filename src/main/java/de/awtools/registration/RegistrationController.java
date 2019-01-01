package de.awtools.registration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The registration controller.
 *
 * @author Andre Winkler
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationController.class);

    private static final String HEADER = "Content-type=application/json;charset=UTF-8";
    private static final String JSON_UTF_8 = "application/json; charset=utf-8";

    
    @Autowired
    private RegistrationService registrationService;

    /**
     * Starts the registration process. The caller receives an email with an URL
     * to confirm his address.
     *
     * @return Web-Service reachable?
     */
    @CrossOrigin
    @GetMapping(path = "/ping", headers = { HEADER }, produces = JSON_UTF_8)
    public JsonObject ping() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("datetime",
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return builder.build();
    }

    @CrossOrigin
    @PostMapping(path = "/register", headers = {
            HEADER }, produces = JSON_UTF_8)
    public String register(@Valid @RequestBody RegistrationJson registration) {

        registrationService.registerNewUserAccount(registration.getNickname(),
                registration.getEmail(), registration.getPassword(),
                registration.getName(), registration.getFirstname(),
                registration.getApplicationId());

        return "{'name': 'TODO'}";
    }

    @CrossOrigin
    @PostMapping(path = "/validate", headers = {
            HEADER }, produces = JSON_UTF_8)
    public String valiateNickname(@RequestBody RegistrationJson registration) {
        return null;
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
