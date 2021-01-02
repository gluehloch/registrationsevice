package de.awtools.registration.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Describes a password.
 * 
 * @author winkler
 */
@Embeddable
public class Password {

    @NotNull
    @Column(name = "password")
    @Size(min = 8, max = 60)
    private String password;

    public Password() {
    }
    
    public Password(String password) {
        this.password = password;
    }
    
    public String get() {
        return password;
    }

    public Password set(String password) {
        this.password = password;
        return this;
    }

}
