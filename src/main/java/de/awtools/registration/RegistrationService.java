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
    private UserRegisterRepository userRegisterRepository;

    @Autowired
    private TimeService timeService;

    @Transactional
    public UserRegistration registerNewUserAccount(String nickname,
            String email, String password, String name, String firstname) {

        // check nickname
        UserRegistration ur = userRegisterRepository.findByNickname(nickname);
        
        
        LocalDateTime now = timeService.now();

        UserRegistration user = new UserRegistration();
        user.setNickname(nickname);
        user.setFirstname(firstname);
        user.setName(name);
        user.setPassword(new Password(passwordEncoder.encode(password)));
        user.setEmail(email);
        user.setCreated(now);
        UUID token = UUID.randomUUID();
        user.setToken(new Token(token.toString()));

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
