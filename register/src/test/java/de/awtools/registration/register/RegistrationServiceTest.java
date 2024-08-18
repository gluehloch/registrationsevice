package de.awtools.registration.register;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

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

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationServiceTest.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationService registrationService;

    @Test
    public void registerNewAccount() throws Exception {
        RegistrationJson registrationJson = new RegistrationJson();
        registrationJson.setNickname("Frosch");
        registrationJson.setEmail("frosch@web.de");
        registrationJson.setPassword("FroschPassword");
        registrationJson.setName("Winkler");
        registrationJson.setFirstname("Andre");
        registrationJson.setAcceptCookie(true);
        registrationJson.setAcceptMail(true);
        registrationJson.setSupplement("Supplement data");
        registrationJson.setApplicationName("applicationName");

        ApplicationEntity application = new ApplicationEntity();
        application.setName("applicationName");
        application.setDescription("Test Application for some JUnit tests.");
        applicationRepository.save(application);
        
        //registrationRepository.deleteAll();
        Optional<RegistrationEntity> validateRegistration = registrationRepository.findByNickname("Frosch");
        validateRegistration.ifPresent(RegistrationServiceTest::throwException);

        DefaultRegistrationValidation validation = registrationService.registerNewAccount(registrationJson);

        RegistrationEntity registration = registrationRepository.findByNickname("Frosch").orElseThrow();
        RegistrationEntity registration2 = registrationRepository.findByToken(registration.getToken()).orElseThrow();

        assertThat(registration.getId()).isEqualTo(registration2.getId());
        assertThat(validation.getValidationCodes()).contains(Validation.ValidationCode.OK);
        assertThat(validation.getNickname()).isEqualTo("Frosch");
        assertThat(registration.isAcceptingCookie()).isTrue();
        assertThat(registration.isAcceptingMail()).isTrue();

        DefaultRegistrationValidation restartUserAccount = registrationService.restartAccount(registrationJson);

        assertThat(restartUserAccount).isNotNull();
        assertThat(restartUserAccount.getNickname()).isEqualTo("Frosch");
        assertThat(restartUserAccount.getValidationCodes()).hasSize(1);
        assertThat(restartUserAccount.getValidationCodes()).contains(Validation.ValidationCode.OK);
    }
    
    public static void throwException(RegistrationEntity registrationEntity) {
    	throw new IllegalStateException(String.format("User registration of user with nickname '%s' shoud not be there.", registrationEntity.getNickname()));
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
