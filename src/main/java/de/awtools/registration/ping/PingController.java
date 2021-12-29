package de.awtools.registration.ping;

import java.time.LocalDateTime;
import java.util.Properties;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.awtools.registration.HttpConst;
import de.awtools.registration.VersionJson;
import de.awtools.registration.time.DateTimeJson;

@RestController
public class PingController {

    private static final String version;
    private static final String artifactId;
    private static final String groupId;

    private static final VersionJson VERSION_JSON;

    static {
        Properties properties = new Properties();
        try {
            properties.load(PingController.class.getResourceAsStream("/pom.properties"));
        } catch (Exception ex) {
            // Ignored.
        }

        version = properties.getProperty("version");
        artifactId = properties.getProperty("artifactId");
        groupId = properties.getProperty("groupId");

        VERSION_JSON = VersionJson.of(groupId, artifactId, version);
    }

    /**
     * Shows the current version of this API.
     *
     * @return A version String.
     */
    @ApiOperation(value = "version", nickname = "version", response = String.class)
    @CrossOrigin
    @GetMapping(path = "/version", produces = HttpConst.JSON_UTF_8)
    public VersionJson versionInfo() {
        return PingController.VERSION_JSON;
    }

    /**
     * Starts the registration process. The caller receives an email with an URL
     * to confirm his address.
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
 
}
