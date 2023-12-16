package de.awtools.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Holds a token to identity the user of a registration process.
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
