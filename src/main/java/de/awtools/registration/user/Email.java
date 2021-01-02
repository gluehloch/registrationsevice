package de.awtools.registration.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Email {

    @NotNull
    @Column(name = "email")
    @Size(min = 8, max = 60)
    private String email;

    Email() {
    }
    
    private Email(String email) {
        this.email = email;
    }

    public String get() {
        return email;
    }

    public void set(String email) {
        this.email = email;
    }

    public static Email of(String email) {
        return new Email(email);
    }
    
}
