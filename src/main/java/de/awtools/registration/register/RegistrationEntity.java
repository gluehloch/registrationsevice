package de.awtools.registration.register;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.awtools.registration.Token;
import org.hibernate.annotations.NaturalId;

import de.awtools.registration.user.Email;
import de.awtools.registration.user.Password;

@Entity(name = "Registration")
@Table(name = "registration")
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NaturalId
    @NotNull
    @Column(name = "nickname")
    private String nickname;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "firstname")
    private String firstname;

    @NotNull
    @Embedded
    private Email email;

    @NotNull
    @Embedded
    private Password password;

    @NotNull
    @Column(name = "created")
    private LocalDateTime created;

    @NotNull
    @Embedded
    private Token token;

    @NotNull
    @Column(name = "application")
    private String application;

    @Column(name = "confirmed")
    private boolean confirmed;

    @Column(name = "acceptmail")
    private boolean acceptMail;

    @Column(name = "acceptcookie")
    private boolean acceptCookie;

    @Column(name = "supplement")
    @Size(max = 100)
    private String supplement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isAcceptingMail() {
        return acceptMail;
    }

    public void setAcceptMail(boolean acceptMail) {
        this.acceptMail = acceptMail;
    }

    public boolean isAcceptingCookie() {
        return acceptCookie;
    }

    public void setAcceptCookie(boolean acceptCookie) {
        this.acceptCookie = acceptCookie;
    }

    public String getSupplement() {
        return supplement;
    }

    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegistrationEntity other = (RegistrationEntity) obj;
        if (nickname == null) {
            if (other.nickname != null)
                return false;
        } else if (!nickname.equals(other.nickname))
            return false;
        return true;
    }

}
