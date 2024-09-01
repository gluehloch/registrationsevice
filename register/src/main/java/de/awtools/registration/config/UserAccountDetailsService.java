package de.awtools.registration.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.awtools.registration.user.UserAccountEntity;
import de.awtools.registration.user.UserAccountRepository;

// @Service
/** TODO: Doppelt hinterlegt @see AuthenticationService ??? */
public class UserAccountDetailsService implements UserDetailsService {

    private final UserAccountRepository userRepository;

    @Autowired
    public UserAccountDetailsService(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) {
        UserAccountEntity user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException(nickname));
        return new UserAccountDetails(user);
    }

    public static class UserAccountDetails implements UserDetails {

        private static final long serialVersionUID = -7882416572804994114L;

        private UserAccountEntity user;

        public UserAccountDetails(UserAccountEntity user) {
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
