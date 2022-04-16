package de.awtools.registration.authentication;

import de.awtools.registration.HttpConst;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    @GetMapping(path = "/validate", headers = { HttpConst.CONTENT_TYPE }, produces = HttpConst.JSON_UTF_8)
    public void validate() {
        // TODO Implement me.
    }

}
