package de.awtools.registration.user;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

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
    private String value;

    @Transient
    private final boolean decoded;

    public Password() {
        decoded = true;
    }
    
    public Password(String password) {
        this.value = password;
        this.decoded = true;
    }

    public Password(String password, boolean decoded) {
        this.value = password;
        this.decoded = decoded;
    }
    
    public String get() {
        return value;
    }

    public Password set(String password) {
        this.value = password;
        return this;
    }

    public boolean isDecoded() {
        return decoded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Password other = (Password) obj;
        return StringUtils.equals(value, other.value);
    }

    public static boolean isEqual(Password p1, Password p2) {
        return p1.equals(p2);
    }
    
    public static Password encoded(String password) {
        return new Password(password, false);
    }

    public static Password decoded(String password) {
        return new Password(password, true);
    }

}
