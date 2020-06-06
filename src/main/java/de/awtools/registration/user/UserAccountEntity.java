package de.awtools.registration.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import de.awtools.registration.Registration;

@Entity(name = "UserAccount")
@Table(name = "useraccount")
public class UserAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "last_change")
    private LocalDateTime lastChange;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "expired")
    private boolean expired;

    @Column(name = "locked")
    private boolean locked;

    @Column(name = "credential_expired")
    private boolean credentialExpired;

    @Column(name = "acceptmail")
    private boolean acceptMail;

    @Column(name = "acceptcookie")
    private boolean acceptCookie;

    @ManyToMany(mappedBy = "userAccounts")
    private Set<ApplicationEntity> applications = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "useraccount_role", joinColumns = @JoinColumn(name = "useraccount_ref"), inverseJoinColumns = @JoinColumn(name = "role_ref"))
    private Set<RoleEntity> roles = new HashSet<>();

    public UserAccountEntity() {
    }

    public UserAccountEntity(LocalDateTime createdAt, Registration registration) {
        this.created = createdAt;
        this.credentialExpired = false;
        this.email = registration.getEmail();
        this.enabled = true;
        this.expired = false;
        this.firstname = registration.getFirstname();
        this.lastChange = this.created;
        this.locked = false;
        this.name = registration.getName();
        this.nickname = registration.getNickname();
        this.password = registration.getPassword();
    }

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

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastChange() {
        return lastChange;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLastChange(LocalDateTime lastChange) {
        this.lastChange = lastChange;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCredentialExpired() {
        return credentialExpired;
    }

    public void setCredentialExpired(boolean credentialExpired) {
        this.credentialExpired = credentialExpired;
    }

    public void setAcceptMail(boolean acceptMail) {
        this.acceptMail = acceptMail;
    }

    public boolean isAcceptingMail() {
        return acceptMail;
    }

    public void setAcceptCookie(boolean acceptCookie) {
        this.acceptCookie = acceptCookie;
    }

    public boolean isAcceptingCookie() {
        return acceptCookie;
    }

    Set<ApplicationEntity> getApplications() {
        return applications;
    }

    public void addRole(RoleEntity role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(RoleEntity role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }
    
    public boolean hasRole(RoleEntity role) {
        return roles.contains(role);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((nickname == null) ? 0 : nickname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserAccountEntity other = (UserAccountEntity) obj;
        if (nickname == null) {
            if (other.nickname != null)
                return false;
        } else if (!nickname.equals(other.nickname))
            return false;
        return true;
    }

    public static class UserAccountBuilder {
        private String nickname;
        private String password;
        private String firstname;
        private String name;
        private LocalDateTime created;
        private Email email;

        private UserAccountBuilder() {
        }

        public static UserAccountBuilder of(String nickname, String password) {
            UserAccountBuilder ub = new UserAccountBuilder();
            ub.nickname = nickname;
            ub.password = password;
            return ub;
        }

        public UserAccountBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserAccountBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }
        
        public UserAccountBuilder created(LocalDateTime localDateTime) {
            this.created = localDateTime;
            return this;
        }
        
        public UserAccountBuilder createdNow() {
            this.created = LocalDateTime.now();
            return this;
        }
        
        public UserAccountBuilder email(Email email) {
            this.email = email;
            return this;
        }

        public UserAccountEntity build() {
            UserAccountEntity ue = new UserAccountEntity();
            ue.setNickname(nickname);
            ue.setPassword(new Password(password));
            ue.setFirstname(firstname);
            ue.setName(name);
            ue.setCreated(created);
            ue.setEmail(email);
            return ue;
        }
    }

}
