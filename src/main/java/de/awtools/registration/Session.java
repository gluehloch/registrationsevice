package de.awtools.registration;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_ref")
    private User user;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLogin() {
        return login;
    }

    public void setLogin(LocalDateTime login) {
        this.login = login;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

}
