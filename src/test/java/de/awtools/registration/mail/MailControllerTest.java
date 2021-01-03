package de.awtools.registration.mail;

import org.junit.jupiter.api.extension.RegisterExtension;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

public class MailControllerTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

}
