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
import de.awtools.registration.user.ApplicationEntity;
import de.awtools.registration.user.ApplicationRepository;
import de.awtools.registration.user.Email;
import de.awtools.registration.user.Password;
import de.awtools.registration.user.UserAccountEntity;

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

        ApplicationEntity application = new ApplicationEntity();
        application.setName("applicationName");
        application.setDescription("Test Application for some JUnit tests.");
        applicationRepository.save(application);

        RegistrationValidation validation = registrationService
                .registerNewAccount("Frosch", "frosch@web.de", "Frosch",
                        "Winkler", "Andre", "applicationName", true, true,
                        "Supplement data");

        RegistrationEntity registration = registrationRepository.findByNickname("Frosch").orElseThrow();
        RegistrationEntity registration2 = registrationRepository.findByToken(registration.getToken()).orElseThrow();

        assertThat(registration.getId()).isEqualTo(registration2.getId());
        assertThat(validation.getValidationCodes()).contains(ValidationCode.OK);
        assertThat(validation.getNickname()).isEqualTo("Frosch");
        assertThat(registration.isAcceptingCookie()).isTrue();
        assertThat(registration.isAcceptingMail()).isTrue();

        RegistrationValidation restartUserAccount = registrationService
                .restartAccount("Frosch", "frosch@web.de", "Frosch",
                        "Winkler", "Andre", "applicationName", true, true,
                        "Supplement data");

        assertThat(restartUserAccount).isNotNull();
        assertThat(restartUserAccount.getNickname()).isEqualTo("Frosch");
        assertThat(restartUserAccount.getValidationCodes()).hasSize(1);
        assertThat(restartUserAccount.getValidationCodes()).contains(ValidationCode.OK);
    }

    @Test
    public void saveNewAccount() {
        ApplicationEntity application = new ApplicationEntity();
        application.setName("applicationId");
        application.setDescription("Test Application for some JUnit tests.");
        applicationRepository.save(application);

        UserAccountEntity userAccount = new UserAccountEntity();
        userAccount.setCreated(LocalDateTime.now());
        userAccount.setCredentialExpired(false);
        userAccount.setEmail(Email.of("frosch@web.de"));
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
