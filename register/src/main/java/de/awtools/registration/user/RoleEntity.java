package de.awtools.registration.user;

import java.util.Collection;
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

    public void addPrivilege(PrivilegeEntity ... privileges) {
        for (PrivilegeEntity privilege : privileges) {
            this.privileges.add(privilege);
            privilege.getRoles().add(this);
        }
    }
    
    public void addPrivileges(Collection<PrivilegeEntity> privileges) {
       privileges.forEach(pv -> addPrivilege(pv)); 
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
