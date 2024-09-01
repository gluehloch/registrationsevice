package de.awtools.registration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Persistence configuration.
 *
 * @author winkler
 */
@Component
public class PersistenceConfiguration {

    @Value("${register.persistence.classname}")
    private String driverClassName;

    @Value("${register.persistence.url}")
    private String url;
    
    @Value("${register.persistence.username}")
    private String username;
    
    @Value("${register.persistence.password}")
    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
