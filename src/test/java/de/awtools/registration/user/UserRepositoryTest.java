package de.awtools.registration.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import de.awtools.registration.Tags;
import de.awtools.registration.config.PersistenceJPAConfig;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@Transactional
@Rollback
class UserRepositoryTest {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @DisplayName("Repository test: Find all users")
    @Test
    @Tag(Tags.REPOSITORY)
    void findUser() {
        UserAccountEntity user = new UserAccountEntity();
        user.setNickname("Frosch");
        user.setFirstname("Andre");
        user.setName("Winkler");
        user.setEmail(new Email("mail@mail.de"));
        user.setCreated(LocalDateTime.now());
        user.setPassword(new Password("Password"));
        user = userAccountRepository.save(user);
        assertThat(user.getId()).isNotNull();

        UserAccountEntity persistedUser = userAccountRepository.findByNickname("Frosch").orElseThrow();
        assertThat(persistedUser).isNotNull();
        assertThat(persistedUser.getFirstname()).isEqualTo("Andre");
        assertThat(persistedUser.getName()).isEqualTo("Winkler");
        assertThat(persistedUser.getNickname()).isEqualTo("Frosch");

        assertThat(persistedUser).isEqualTo(user);
    }

    @DisplayName("Repository test: Find all roles of a user")
    @Test
    @Tag(Tags.REPOSITORY)
    void findRoles() {
        UserAccountEntity frosch = UserAccountEntity.UserAccountBuilder
                .of("Frosch", "PasswordFrosch")
                .firstname("Andre")
                .name("Winkler")
                .created(LocalDateTime.now())
                .email(new Email("mail@mail.de"))
                .build();
        userAccountRepository.save(frosch);

        RoleEntity role = new RoleEntity();
        role.setName("USER");
        roleRepository.save(role);

        frosch.addRole(role);

        List<RoleEntity> roles = roleRepository.findRoles("Frosch");
        assertThat(roles).hasSize(1);
        assertThat(roles.get(0).getName()).isEqualTo("USER");
    }

    @DisplayName("Repository test: Find all privilegs of a user")
    @Test
    @Tag(Tags.REPOSITORY)
    void findPrivileges() {
        PrivilegeEntity readPriv = PrivilegeEntity.PrivilegeBuilder.of("READ_PRIV");
        PrivilegeEntity persistedReadPriv = privilegeRepository.save(readPriv);
        assertThat(persistedReadPriv.getName()).isEqualTo("READ_PRIV");
        
        PrivilegeEntity writePriv = PrivilegeEntity.PrivilegeBuilder.of("WRITE_PRIV");
        privilegeRepository.save(writePriv);
        
        UserAccountEntity frosch = UserAccountEntity.UserAccountBuilder
                .of("Frosch", "PasswordFrosch")
                .firstname("Andre")
                .name("Winkler")
                .created(LocalDateTime.now())
                .email(new Email("mail@mail.de"))
                .build();
        
        RoleEntity role = RoleEntity.RoleBuilder.of("ADMIN");
        role.addPrivilege(readPriv);
        role.addPrivilege(writePriv);
        roleRepository.save(role); 
        frosch.addRole(role);
        userAccountRepository.save(frosch);
        
        UserAccountEntity user = userAccountRepository.findByNickname("Frosch").orElseThrow();
        assertThat(user.hasRole(role)).isTrue();
    }

}
