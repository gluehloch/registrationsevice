package de.awtools.registration.authentication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.awtools.registration.config.PersistenceJPAConfig;
import de.awtools.registration.register.RegistrationJson;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@WebAppConfiguration
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthenticationController(authenticationService))
                .build();
    }

    @Tag("authentication")
    @Tag("controller")
    @DisplayName("login and logout")
    @Test
    void loginLogout() throws Exception {
//        RegistrationJson registration = new RegistrationJson();
//
//        String requestJson = toString(registration);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nickname", "myNickname")
                        .param("password", "myPassword"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("validationCodes.*", Matchers.hasSize(7)))
                .andExpect(jsonPath("validationCodes.*", Matchers.containsInAnyOrder("UNKNOWN_APPLICATION",
                        "MISSING_ACCEPT_EMAIL", "MISSING_ACCEPT_COOKIE", "NICKNAME_IS_EMPTY", "PASSWORD_TOO_SHORT",
                        "EMAIL_IS_EMPTY", "FIRSTNAME_IS_EMPTY")));
    }

}
