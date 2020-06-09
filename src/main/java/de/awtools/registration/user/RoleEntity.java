package de.awtools.registration.user;

import java.util.HashSet;
import java.util.Objects;
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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

@Entity(name = "Role")
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NaturalId
    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserAccountEntity> users = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "role_privilege", joinColumns = @JoinColumn(name = "role_ref", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_ref", referencedColumnName = "id"))
    private Set<PrivilegeEntity> privileges = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public Set<UserAccountEntity> getUsers() {
        return users;
    }

    public void addPrivilege(PrivilegeEntity privilege) {
        privileges.add(privilege);
        privilege.getRoles().add(this);
    }

    public Set<PrivilegeEntity> getPrivileges() {
        return privileges;
    }
    
    public boolean hasPrivilege(PrivilegeEntity privilege) {
        return privileges.contains(privilege);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof RoleEntity)) {
            return false;
        }

        RoleEntity other = (RoleEntity) o;
        return Objects.equals(getName(), other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public static class RoleBuilder {
        private RoleBuilder() {
        }

        public static RoleEntity of(String name) {
            RoleEntity role = new RoleEntity();
            role.setName(name);
            return role;
        }
    }

}
