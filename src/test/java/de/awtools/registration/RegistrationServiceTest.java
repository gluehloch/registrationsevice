package de.awtools.registration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private RegistrationService registrationService;

    @Test
    public void testRegistrationService() throws Exception {
        Application application = new Application();
        application.setName("applicationId");
        applicationRepository.save(application);

        Registration account = registrationService
                .registerNewUserAccount("Frosch", "frosch@web.de", "Frosch",
                        "Winkler", "Andre", "applicationId");

        assertThat(account.getPassword().get()).isNotEqualTo("Frosch");

    }

}
