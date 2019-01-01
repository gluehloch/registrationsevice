package de.awtools.registration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

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
    private RegistrationService registrationService;

    @Test
    public void testRegistrationService() throws Exception {
        LOG.info("Start of the test...");
        
        Application application = new Application();
        application.setName("applicationId");
        application.setDescription("Test Application for some JUnit tests.");
        applicationRepository.save(application);

        Registration account = registrationService
                .registerNewUserAccount("Frosch", "frosch@web.de", "Frosch",
                        "Winkler", "Andre", "applicationId");

        assertThat(account.getPassword().get()).isNotEqualTo("Frosch");
        
        UserAccount userAccount = new UserAccount();
        userAccount.setCreated(LocalDateTime.now());
        userAccount.setCredentialExpired(false);
        userAccount.setEmail("frosch@web.de");
        userAccount.setEnabled(true);
        userAccount.setExpired(false);
        userAccount.setFirstname("Andre");
        userAccount.setLastChange(LocalDateTime.now());
        userAccount.setLocked(false);
        userAccount.setName("Winkler");
        userAccount.setNickname("Frosch");
        userAccount.setPassword(account.getPassword());
        application.addUser(userAccount);
        applicationRepository.save(application);
    }

}
