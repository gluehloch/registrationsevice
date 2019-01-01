package de.awtools.registration;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private RegisterRepository userRegisterRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TimeService timeService;

    /**
     * Starts the registration process.
     * 
     * @param nickname nickname 
     * @param email the email address
     * @param password password
     * @param name real name
     * @param firstname real firstname
     * @param application the application to register for
     * @return UserRegistration
     */
    @Transactional
    public Registration registerNewUserAccount(String nickname,
            String email, String password, String name, String firstname,
            String application) {

        // check application
        
        Application app = applicationRepository.findByName(application);
        if (app == null) {
            throw new IllegalArgumentException("Unknown application: " + application);
        }
        
        // TODO
        
        // check nickname
        Registration ur = userRegisterRepository.findByNickname(nickname);

        LocalDateTime now = timeService.now();

        Registration user = new Registration();
        user.setNickname(nickname);
        user.setFirstname(firstname);
        user.setName(name);
        user.setPassword(new Password(passwordEncoder.encode(password)));
        user.setEmail(email);
        user.setCreated(now);
        UUID token = UUID.randomUUID();
        user.setToken(new Token(token.toString()));
        user.setApplication(application);

        /*
         * user.setCredentialExpired(false); user.setEnabled(true);
         * user.setLastChange(now); user.setLocked(false);
         */

        userRegisterRepository.save(user);

        return user;
    }

    @Transactional
    public void confirmAccount(String token) {
        // TODO Auto-generated method stub

    }

    @Autowired
    private RegistrationDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

}
