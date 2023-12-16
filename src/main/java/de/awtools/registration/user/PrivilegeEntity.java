package de.awtools.registration.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

@Entity(name = "Privilege")
@Table(name = "privilege")
public class PrivilegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NaturalId
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<RoleEntity> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PrivilegeEntity)) {
            return false;
        }

        PrivilegeEntity other = (PrivilegeEntity) o;
        return Objects.equals(getName(), other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public static class PrivilegeBuilder {
        private PrivilegeBuilder() {
        }

        public static PrivilegeEntity of(String name) {
            PrivilegeEntity privilege = new PrivilegeEntity();
            privilege.setName(name);
            return privilege;
        }
    }

}
