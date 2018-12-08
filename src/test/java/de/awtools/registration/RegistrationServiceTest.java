package de.awtools.registration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
// @WebMvcTest(RegistrationController.class)
@ComponentScan("de.awtools.registration")
@Transactional
@Rollback
public class RegistrationServiceTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Autowired
    private RegistrationService registrationService;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext)
                .build();
    }

    @Test
    public void testRegistrationService() {
        UserRegistration account = registrationService
                .registerNewUserAccount("Frosch", "frosch@web.de", "Frosch",
                        "Winkler", "Andre");

        assertThat(account.getPassword().get()).isNotEqualTo("Frosch");

    }

}
