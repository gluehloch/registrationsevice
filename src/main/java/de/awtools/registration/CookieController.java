package de.awtools.registration;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @Valid @RequestBody CookieJson cookieJson) {
        DateTimeJson dateTimeJson = new DateTimeJson();
        return dateTimeJson.setDateTime(cookieService
                .storeCookieAcceptance(cookieJson.getBrowser(),
                        cookieJson.getRemoteaddress(),
                        cookieJson.isAcceptCookies())
                .getCreated());
    }

}
