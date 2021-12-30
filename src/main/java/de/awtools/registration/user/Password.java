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
 * <ul>
 * <li>encode: etw. verschluesseln</li>
 * <li>encoded: Adjektiv verschluesselt </li>
 * <li>decode: etw. entschluesseln</li>
 * <li>decoded: Adjekktiv entschluesselt</li>
 * </ul>
 *
 * 
 * @author winkler
 */
@Embeddable
public class Password {

    @NotNull
    @Column(name = "password")
    @Size(min = 8, max = 60)
    private String value;

    /**
     * Der Default schlaegt vor, verschluesselte Passwoerter hier abzulegen. Alles andere eroeffnet Sicherheitsluecken.
     */
    @Transient
    private final boolean encoded;

    public Password() {
        encoded = true;
    }
    
    public Password(String password) {
        this.value = password;
        this.encoded = true;
    }

    public Password(String password, boolean encoded) {
        this.value = password;
        this.encoded = encoded;
    }
    
    public String get() {
        return value;
    }

    public Password set(String password) {
        this.value = password;
        return this;
    }

    /**
     * Ist das Passwort verschluesselt?
     *
     * @return <code>true</code>, wenn das Passwort verschluesselt ist.
     */
    public boolean isEncoded() {
        return encoded;
    }

    /**
     * Ist das Passwort entschluesselt?
     *
     * @return <code>true</code>, wenn das Passwort entschluesselt vorliegt.
     */
    public boolean isDecoded() {
        return !encoded;
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
        if (p1.isEncoded() && p2.isEncoded()) {
            return p1.equals(p2);
        } else {
            return false;
        }
    }

    /**
     * Erstellt ein Password.
     *
     * @param password Das Passwort. Dieses ist bereits ge-hasht!
     * @return Ein Password.
     */
    public static Password encoded(String password) {
        return new Password(password, true);
    }

    /**
     * Erstellt ein Password.
     *
     * @param password Das Passwort. Dieses ist NICHT ge-hasht oder NICHT verschluesselt.
     * @return Das Password.
     */
    public static Password decoded(String password) {
        return new Password(password, false);
    }

}
