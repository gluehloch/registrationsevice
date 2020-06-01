package de.awtools.registration.cookie;

import java.time.LocalDateTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.awtools.registration.HttpConst;
import de.awtools.registration.time.DateTimeJson;
import io.swagger.annotations.ApiOperation;

/**
 * The controller to store cookie confirmation or cancellation.
 *
 * @author Andre Winkler
 */
@RestController
@RequestMapping("/cookie")
public class CookieController {

    @Autowired
    private CookieService cookieService;

    /**
     * Ping the cookie service.
     *
     * @return Web-Service reachable?
     */
    @ApiOperation(value = "ping", nickname = "ping", response = DateTimeJson.class, notes = "Ping this service. Is it reachable?")
    @CrossOrigin
    @GetMapping(path = "/ping", produces = HttpConst.JSON_UTF_8)
    public DateTimeJson ping() {
        DateTimeJson dateTimeJson = new DateTimeJson();
        return dateTimeJson.setDateTime(LocalDateTime.now());
    }

    @ApiOperation(value = "confirmCookie", nickname = "Confirm Cookie", response = DateTimeJson.class, notes = "The user confirmed a cookie.")
    @CrossOrigin
    @PostMapping(path = "confirmCookie", headers = {
            HttpConst.HEADER }, produces = HttpConst.JSON_UTF_8)
    public DateTimeJson confirmCookie(
            @Valid @RequestBody CookieJson cookieJson,
            @RequestHeader("User-Agent") String userAgent,
            HttpServletRequest request) {
    	
    	Cookie[] cookies = request.getCookies();
    	
        DateTimeJson dateTimeJson = new DateTimeJson();      
        
        return dateTimeJson.setDateTime(cookieService
                .storeCookieAcceptance(
                        cookieJson.getWebsite(),
                        userAgent,
                        request.getRemoteAddr(),
                        cookieJson.isAcceptCookies())
                .getCreated());
    }

}