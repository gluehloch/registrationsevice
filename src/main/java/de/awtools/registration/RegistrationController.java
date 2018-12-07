package de.awtools.registration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @CrossOrigin
    @GetMapping(path = "/ping")
    public String ping() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @CrossOrigin
    @PostMapping(path = "/register", headers = {
            "Content-type=application/json" })
    public String register(@Valid @RequestBody RegistrationJson registration) {

        registrationService.registerNewUserAccount(registration.getNickname(),
                registration.getEmail(), registration.getPassword(),
                registration.getName(), registration.getFirstname());
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
