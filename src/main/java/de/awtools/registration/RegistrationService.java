package de.awtools.registration;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.awtools.registration.RegistrationValidationJson.ValidationCode;

/**
 * Register and confirm a new user.
 * 
 * @author winkler
 */
@Service
public class RegistrationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private RegistrationDetailsService userDetailsService;

    @Autowired
    private TimeService timeService;

    /**
     * Starts the registration process.
     * 
     * @param nickname
     *            nickname
     * @param email
     *            the email address
     * @param password
     *            password
     * @param name
     *            real name
     * @param firstname
     *            real firstname
     * @param application
     *            the application to register for
     * @return UserRegistration
     */
    @Transactional
    public Registration registerNewUserAccount(String nickname,
            String email, String password, String name, String firstname,
            String application) {

        Application app = applicationRepository.findByName(application);
        if (app == null) {
            throw new IllegalArgumentException(
                    "Unknown application: " + application);
        }

        Registration registrationDefined = registrationRepository.findByNickname(nickname);
        if (registrationDefined != null) {
            throw new IllegalArgumentException(
                    "Nickname is already defined: " + nickname);
        }

        LocalDateTime now = timeService.now();

        Registration registration = new Registration();
        registration.setNickname(nickname);
        registration.setFirstname(firstname);
        registration.setName(name);
        registration.setPassword(new Password(passwordEncoder.encode(password)));
        registration.setEmail(email);
        registration.setCreated(now);
        UUID token = UUID.randomUUID();
        registration.setToken(new Token(token.toString()));
        registration.setApplication(application);
        registration.setConfirmed(false);

        /*
         * user.setCredentialExpired(false); user.setEnabled(true);
         * user.setLastChange(now); user.setLocked(false);
         */

        registrationRepository.save(registration);

        return registration;
    }

    @Transactional
    public void confirmAccount(String token) {
        // TODO Auto-generated method stub

    }

    @Transactional
    public RegistrationValidationJson validate(String nickname, String email,
            String applicationName) {

        Application application = applicationRepository.findByName(applicationName);
        if (application == null) {
            return new RegistrationValidationJson(ValidationCode.ILLEGAL_ARGUMENTS);
        }

        Registration registrationDefined = null; 
        
        registrationDefined = registrationRepository.findByNickname(nickname);
        if (registrationDefined != null) {
            return new RegistrationValidationJson(ValidationCode.KNOWN_NICKNAME);
        }
        
        registrationDefined = registrationRepository.findByEmail(email);
        if (registrationDefined != null) {
            return new RegistrationValidationJson(ValidationCode.KNOWN_MAILADDRESS);
        }
        
        return new RegistrationValidationJson(ValidationCode.OK);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

}
