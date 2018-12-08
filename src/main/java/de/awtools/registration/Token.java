package de.awtools.registration;

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
public class Token {

    @NotNull
    @Column(name = "token")
    @Size(min = 8, max = 60)
    private String token;

    public Token() {
    }
    
    public Token(String token) {
        this.token = token;
    }
    
    public String get() {
        return token;
    }

    public Token set(String token) {
        this.token = token;
        return this;
    }

}
