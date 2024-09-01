package de.awtools.registration.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

/**
 * The application the user is registered.
 *
 * @author winkler
 */
@Entity(name = "Application")
@Table(name = "application")
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NaturalId
    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany /* (cascade = { CascadeType.ALL }) */
    @JoinTable(name = "useraccount_application", joinColumns = {
            @JoinColumn(name = "application_ref") }, inverseJoinColumns = {
                    @JoinColumn(name = "useraccount_ref") })
    private Set<UserAccountEntity> userAccounts = new HashSet<>();

    public Long getId() {
        return id;
    }

    void setId(Long id) {
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

    public void addUser(UserAccountEntity userAccount) {
        userAccount.getApplications().add(this);
        userAccounts.add(userAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApplicationEntity other = (ApplicationEntity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public static class ApplicationBuilder {
        private String name;
        public static ApplicationBuilder of(String name) {
            ApplicationBuilder builder = new ApplicationBuilder();
            builder.name = name;
            return builder;
        }
        
        public ApplicationEntity description(String description) {
            ApplicationEntity app = new ApplicationEntity();
            app.setName(name);
            app.setDescription(description);
            return app;
        }
    }
    
}
