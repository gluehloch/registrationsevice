package de.awtools.registration.assets;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.awtools.registration.HttpConst;
import de.awtools.registration.time.DateTimeJson;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping("/assets")
public class AssetsController {

    /**
     * Ping the asset service.
     *
     * @return Web-Service reachable?
     */
    @ApiOperation(value = "ping", nickname = "ping", response = DateTimeJson.class, notes = "Ping this service. Is it reachable?")
    @GetMapping(path = "/ping", produces = HttpConst.JSON_UTF_8)
    public DateTimeJson ping() {
        DateTimeJson dateTimeJson = new DateTimeJson();
        return dateTimeJson.setDateTime(LocalDateTime.now());
    }

    @ApiOperation(value = "assets", nickname = "assets", response = String.class, notes = "Get some static assets.")
	@GetMapping(path = "/{resource}", produces = HttpConst.JSON_UTF_8)
	public String resource(@PathVariable String resource) throws IOException {
        try (InputStream resourceAsStream = AssetsController.class.getResourceAsStream("/assets/" + resource)) {
            return new String(requireNonNull(resourceAsStream).readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}
