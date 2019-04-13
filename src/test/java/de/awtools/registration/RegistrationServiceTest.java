package de.awtools.registration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import de.awtools.registration.RegistrationValidation.ValidationCode;
import de.awtools.registration.config.PersistenceJPAConfig;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@Transactional
@Rollback
public class RegistrationServiceTest {

    private static final Logger LOG = LogManager.getLogger();
    
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationService registrationService;

    @Test
    public void registerNewAccount() throws Exception {
        LOG.info("Start of the test...");

        Application application = new Application();
        application.setName("applicationName");
        application.setDescription("Test Application for some JUnit tests.");
        applicationRepository.save(application);

        RegistrationValidation validation = registrationService
                .registerNewUserAccount("Frosch", "frosch@web.de", "Frosch",
                        "Winkler", "Andre", "applicationName", true, true);

        Registration registration = registrationRepository
                .findByNickname("Frosch");
        Registration registration2 = registrationRepository
                .findByToken(registration.getToken());

        assertThat(registration.getId()).isEqualTo(registration2.getId());
        assertThat(validation.getValidationCode()).isEqualTo(ValidationCode.OK);
        assertThat(validation.getNickname()).isEqualTo("Frosch");
        assertThat(registration.isAcceptingCookie()).isTrue();
        assertThat(registration.isAcceptingMail()).isTrue();

        RegistrationValidation restartUserAccount = registrationService
                .restartUserAccount("Frosch", "frosch@web.de", "Frosch",
                        "Winkler", "Andre", "applicationName", true, true);
    }

    @Test
    public void saveNewAccount() {
        Application application = new Application();
        application.setName("applicationId");
        application.setDescription("Test Application for some JUnit tests.");
        applicationRepository.save(application);

        UserAccount userAccount = new UserAccount();
        userAccount.setCreated(LocalDateTime.now());
        userAccount.setCredentialExpired(false);
        userAccount.setEmail(new Email("frosch@web.de"));
        userAccount.setEnabled(true);
        userAccount.setExpired(false);
        userAccount.setFirstname("Andre");
        userAccount.setLastChange(LocalDateTime.now());
        userAccount.setLocked(false);
        userAccount.setName("Winkler");
        userAccount.setNickname("Frosch");
        userAccount.setPassword(new Password("password"));
        application.addUser(userAccount);
        applicationRepository.save(application);
    }

}
