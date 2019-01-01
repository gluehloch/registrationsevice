package de.awtools.registration;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

/**
 * The application the user is registered.
 *
 * @author winkler
 */
@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NaturalId
    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    
    @ManyToMany /* (cascade = { CascadeType.ALL }) */
    @JoinTable(
            name = "useraccount_application",
            joinColumns = { @JoinColumn(name = "application_ref")},
            inverseJoinColumns = { @JoinColumn(name = "useraccount_ref")})
    private Set<UserAccount> userAccounts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void addUser(UserAccount userAccount) {
        userAccount.getApplications().add(this);
        userAccounts.add(userAccount);
    }

}
