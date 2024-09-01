package de.awtools.registration;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import de.awtools.registration.user.UserAccountEntity;

@Entity
@Table(name = "useraccount_session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_ref")
    private UserAccountEntity user;

    @NotNull
    @Column(name = "login")
    private LocalDateTime login;

    @Null
    @Column(name = "logout")
    private LocalDateTime logout;

    @NotNull
    @Column(name = "token")
    private String token;

    @NotNull
    @Column(name = "sessionId")
    private String sessionId;

    @Column(name = "remoteaddress")
    private String remoteaddress;

    @Column(name = "browser")
    private String browser;

    public Long getId() {
        return id;
    }

    public LocalDateTime getLogin() {
        return login;
    }

    public void setLogin(LocalDateTime login) {
        this.login = login;
    }

    public UserAccountEntity getUser() {
        return user;
    }

    public void setUser(UserAccountEntity user) {
        this.user = user;
    }

    public LocalDateTime getLogout() {
        return logout;
    }

    public void setLogout(LocalDateTime logout) {
        this.logout = logout;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRemoteaddress() {
        return remoteaddress;
    }

    public void setRemoteaddress(String remoteaddress) {
        this.remoteaddress = remoteaddress;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Session other = (Session) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
