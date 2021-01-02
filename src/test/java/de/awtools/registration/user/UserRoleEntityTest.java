package de.awtools.registration.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.awtools.registration.user.UserAccountEntity.UserAccountBuilder;

class UserRoleEntityTest {

    @Test
    void userRole() {
        UserAccountEntity frosch = UserAccountBuilder
                .of("Frosch", "PasswordFrosch")
                .firstname("Andre")
                .name("Winkler")
                .build();

        RoleEntity userRole = RoleEntity.RoleBuilder.of("USER");
        RoleEntity adminRole = RoleEntity.RoleBuilder.of("ADMIN");

        frosch.addRole(userRole);
        frosch.addRole(adminRole);
        assertThat(frosch.getRoles()).hasSize(2);
        frosch.addRole(userRole);
        frosch.addRole(adminRole);
        assertThat(frosch.getRoles()).hasSize(2);

        frosch.removeRole(userRole);
        assertThat(frosch.getRoles()).hasSize(1);
        frosch.removeRole(adminRole);
        assertThat(frosch.getRoles()).hasSize(0);

        frosch.removeRole(userRole);
        frosch.removeRole(adminRole);
        assertThat(frosch.getRoles()).hasSize(0);
    }
}
