package de.awtools.registration;

import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller to store cookie confirmation or cancellation.
 *
 * @author Andre Winkler
 */
@RestController
@RequestMapping("/cookie")
public class CookieController {

    private static final Logger LOG = LogManager.getLogger();

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
        // LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        dateTimeJson.setDateTime(LocalDateTime.now());
        return dateTimeJson;
    }

    @ApiOperation(value = "confirmCookie", nickname = "Confirm Cookie", response = String.class, notes = "The user confirmed a cookie.")
    @CrossOrigin
    @PostMapping(path = "confirmCookie", headers = {
            HttpConst.HEADER }, produces = HttpConst.JSON_UTF_8)
    public DateTimeJson confirmCookie() {
        DateTimeJson dateTimeJson = new DateTimeJson();
        dateTimeJson.setDateTime(LocalDateTime.now());
        return dateTimeJson;
    }

}
