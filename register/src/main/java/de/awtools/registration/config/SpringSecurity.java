package de.awtools.registration.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import de.awtools.registration.user.UserAccountRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurity {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserAccountDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Funktioniert dieses Konstrukt aus @Bean und @Autowired?
     *
     * @param userDetailsService ...
     * @return ...
     */
    @Bean
    public DaoAuthenticationProvider authProvider(@Autowired UserDetailsService userDetailsService, @Autowired PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }

    /*
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    */

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserAccountDetailsService(userAccountRepository);
        /* 
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
         return new InMemoryUserDetailsManager(user);
         */
     }

     @Bean
    public SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authz -> authz /*.anyRequest().permitAll()*/
                .requestMatchers(antMatcher(HttpMethod.GET, "register/**")).permitAll());
            // Authentication
            /*
            .requestMatchers(antMatcher(HttpMethod.GET,    BetofficeUrlPath.URL_AUTHENTICATION + BetofficeUrlPath.URL_AUTHENTICATION_PING)).permitAll()
            .requestMatchers(antMatcher(HttpMethod.POST,   BetofficeUrlPath.URL_AUTHENTICATION + BetofficeUrlPath.URL_AUTHENTICATION_LOGIN)).permitAll()
            .requestMatchers(antMatcher(HttpMethod.POST,   BetofficeUrlPath.URL_AUTHENTICATION + BetofficeUrlPath.URL_AUTHENTICATION_LOGOUT)).authenticated()
            */
            // Send tipp form
            // .requestMatchers(antMatcher(HttpMethod.POST,   BetofficeUrlPath.URL_OFFICE + "/tipp/submit")).hasRole("TIPPER")
            // Administration
            /* 
            .requestMatchers(antMatcher(HttpMethod.GET,    BetofficeUrlPath.URL_ADMIM + "/**")).hasRole("ADMIN")
            .requestMatchers(antMatcher(HttpMethod.PUT,    BetofficeUrlPath.URL_ADMIM + "/**")).hasRole("ADMIN")
            .requestMatchers(antMatcher(HttpMethod.POST,   BetofficeUrlPath.URL_ADMIM + "/**")).hasRole("ADMIN")
            .requestMatchers(antMatcher(HttpMethod.DELETE, BetofficeUrlPath.URL_ADMIM + "/**")).hasRole("ADMIN")
            */
            // TODO Brauche ich das auch noch???
            // http.addFilter(new JWTAuthenticationFilter(authenticationManager, authService));
            // http.addFilter(new JWTAuthorizationFilter(authenticationManager, authService));

            return http.build();
    }

}
