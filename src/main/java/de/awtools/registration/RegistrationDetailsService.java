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
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new RegistrationUserDeatils(user);
    }
    
    
    public static class RegistrationUserDeatils implements UserDetails {

        private static final long serialVersionUID = -7882416572804994114L;

        private User user;

        public RegistrationUserDeatils(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // TODO Implement some authorities!
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

