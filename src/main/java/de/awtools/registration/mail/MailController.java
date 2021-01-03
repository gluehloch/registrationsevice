package de.awtools.registration.mail;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.awtools.registration.HttpConst;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/mail")
public class MailController {

    private static final Logger LOG = LogManager.getLogger();

    private final SendMail sendMail;
    
    public MailController(SendMail sendMail) {
        this.sendMail = sendMail;
    }

    @ApiOperation(value = "submit", notes = "Starts the registration process")
    @CrossOrigin
    @PostMapping(path = "/submit", headers = { HttpConst.HEADER }, produces = HttpConst.JSON_UTF_8)
    public ResponseEntity<Boolean> register() {
        final String from = "mail@andre-winkler.de";
        final String recipient = "gluehloch@googlemail.com";
        final String subject = "Test";
        final String messageText = "Das ist ein Test.";
        
        try {
            sendMail.sendMail(from, recipient, subject, messageText);
        } catch (MessagingException ex) {
            LOG.error(ex);
        }
        
        return ResponseEntity.ok(Boolean.TRUE);
    }
    
}
