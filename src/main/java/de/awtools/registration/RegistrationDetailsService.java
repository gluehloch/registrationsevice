package de.awtools.registration;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RegistrationDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) {
        UserAccount user = userRepository.findByNickname(nickname);
        if (user == null) {
            throw new UsernameNotFoundException(nickname);
        }
        return new RegistrationUserDeatils(user);
    }
    
    
    public static class RegistrationUserDeatils implements UserDetails {

        private static final long serialVersionUID = -7882416572804994114L;

        private UserAccount user;

        public RegistrationUserDeatils(UserAccount user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // TODO Implement some authorities! Sind das die Rolle und Zustaendigkeiten?
            return null;
        }

        @Override
        public String getPassword() {
            return user.getPassword().get();
        }

        @Override
        public String getUsername() {
            return user.getNickname();
        }

        @Override
        public boolean isAccountNonExpired() {
            return !user.isExpired();
        }

        @Override
        public boolean isAccountNonLocked() {
            return !user.isLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return !user.isCredentialExpired();
        }

        @Override
        public boolean isEnabled() {
            return user.isEnabled();
        }
    }

}

