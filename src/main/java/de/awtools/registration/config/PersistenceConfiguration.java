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

    @Value("${register.persistence.classname:org.mariadb.jdbc.Driver}")
    private String driverClassName;

    @Value("${register.persistence.url:jdbc:mariadb://192.168.99.100:3308/register")
    private String url;
    
    @Value("${register.persistence.username:register}")
    private String username;
    
    @Value("${register.persistence.password:register}")
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
