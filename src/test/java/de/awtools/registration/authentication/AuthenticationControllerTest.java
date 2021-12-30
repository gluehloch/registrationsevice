package de.awtools.registration.authentication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

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
import de.awtools.registration.password.PasswordEncoderWrapper;
import de.awtools.registration.user.Email;
import de.awtools.registration.user.Password;
import de.awtools.registration.user.UserAccountEntity;
import de.awtools.registration.user.UserAccountRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@WebAppConfiguration
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoderWrapper passwordEncoderWrapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthenticationController(authenticationService))
                .build();
    }

    @Tag("authentication")
    @Tag("controller")
    @DisplayName("login failed, nickname is unknown")
    @Test
    void loginFailedUnknownUser() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nickname", "myNickname")
                        .param("password", "myPassword"))
                .andDo(print())
                .andExpect(status().isForbidden());
//                .andExpect(jsonPath("validationCodes.*", Matchers.hasSize(7)))
//                .andExpect(jsonPath("validationCodes.*", Matchers.containsInAnyOrder("UNKNOWN_APPLICATION",
//                        "MISSING_ACCEPT_EMAIL", "MISSING_ACCEPT_COOKIE", "NICKNAME_IS_EMPTY", "PASSWORD_TOO_SHORT",
//                        "EMAIL_IS_EMPTY", "FIRSTNAME_IS_EMPTY")));
    }

    @Tag("authentication")
    @Tag("controller")
    @DisplayName("login logout successful")
    @Test
    @Transactional
    void loginSuccessful() throws Exception {
        UserAccountEntity userAccount = new UserAccountEntity();
        userAccount.setFirstname("firstname");
        userAccount.setName("name");
        userAccount.setNickname("nickname");
        userAccount.setEmail(Email.of("mail@mail.de"));
        userAccount.setPassword(passwordEncoderWrapper.encode(Password.of("password")));
        userAccount.setLocked(false);
        userAccount.setEnabled(true);
        userAccount.setCreated(LocalDateTime.now());
        userAccountRepository.save(userAccount);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nickname", "nickname")
                        .param("password", "password"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
