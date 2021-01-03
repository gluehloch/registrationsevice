package de.awtools.registration.mail;

import org.junit.jupiter.api.extension.RegisterExtension;

public class MailControllerTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

}
