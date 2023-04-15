package de.awtools.registration.assets;

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

    /*
    @CrossOrigin
	@GetMapping(name = "/x/{resource}", produces = HttpConst.JSON_UTF_8)
	public HttpEntity<String> resource(@PathVariable String resource) throws IOException {
		InputStream resourceAsStream = AssetsController.class.getResourceAsStream(resource);
		return new HttpEntity<String>(new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8));
	}
	*/

    @CrossOrigin
	@GetMapping(name = "/xxx", produces = HttpConst.JSON_UTF_8)
	public String resourcex(@PathVariable String resource) throws IOException {
    	return "xxx";
    }

    @CrossOrigin
	@GetMapping(name = "/zzz/{resource}", produces = HttpConst.JSON_UTF_8)
	public String resource(@PathVariable String resource) throws IOException {
		InputStream resourceAsStream = AssetsController.class.getResourceAsStream(resource);
		return new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
	}

}
