package de.awtools.registration.user;

import static de.awtools.registration.user.UserAccountEntity.UserAccountBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
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
import de.awtools.registration.user.ApplicationEntity.ApplicationBuilder;
import de.awtools.registration.user.PrivilegeEntity.PrivilegeBuilder;
import de.awtools.registration.user.RoleEntity.RoleBuilder;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@Transactional
@Rollback
public class BetofficeApplicationUserRoleTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    void before() {
        ApplicationEntity betofficeApplication = ApplicationBuilder.of("betoffice").description("Betoffice Community");
        applicationRepository.save(betofficeApplication);

        PrivilegeEntity createSeason = PrivilegeBuilder.of("CREATE season");
        PrivilegeEntity updateSeason = PrivilegeBuilder.of("UPDATE season");
        PrivilegeEntity deleteSeason = PrivilegeBuilder.of("DELETE season");
        PrivilegeEntity readSeason = PrivilegeBuilder.of("READ season");

        PrivilegeEntity createRound = PrivilegeBuilder.of("CREATE round");
        PrivilegeEntity updateRound = PrivilegeBuilder.of("UPDATE round");
        PrivilegeEntity deleteRound = PrivilegeBuilder.of("DELETE round");
        PrivilegeEntity readRound = PrivilegeBuilder.of("READ round");

        PrivilegeEntity createGame = PrivilegeBuilder.of("CREATE game");
        PrivilegeEntity updateGame = PrivilegeBuilder.of("UPDATE game");
        PrivilegeEntity deleteGame = PrivilegeBuilder.of("DELETE game");
        PrivilegeEntity readGame = PrivilegeBuilder.of("READ game");

        privilegeRepository.saveAll(
                List.of(createSeason, updateSeason, deleteSeason,
                        createRound, updateRound, deleteRound, readRound,
                        createGame, updateGame, deleteGame, readGame));

        PrivilegeEntity createTipp = PrivilegeBuilder.of("CREATE tipp");
        PrivilegeEntity updateTipp = PrivilegeBuilder.of("UPDATE tipp");
        PrivilegeEntity deleteTipp = PrivilegeBuilder.of("DELETE tipp");
        PrivilegeEntity readTipp = PrivilegeBuilder.of("READ tipp");

        PrivilegeEntity createCommunity = PrivilegeBuilder.of("CREATE community");
        PrivilegeEntity updateCommunity = PrivilegeBuilder.of("UPDATE community");
        PrivilegeEntity deleteCommunity = PrivilegeBuilder.of("DELETE community");
        PrivilegeEntity readCommunity = PrivilegeBuilder.of("READ community");
        
        List<PrivilegeEntity> createUpdateDeleteReadSeason =
            List.of(createSeason, updateSeason, deleteSeason,
                createRound, updateRound, deleteRound, readRound,
                createGame, updateGame, deleteGame, readGame);
        privilegeRepository.saveAll(
                List.of(createTipp, updateTipp, deleteTipp, readTipp));

        RoleEntity adminRole = RoleBuilder.of("ADMIN");
        RoleEntity communityManagerRole = RoleBuilder.of("Community Manager");
        RoleEntity tipperRole = RoleBuilder.of("Tipper");
        
        roleRepository.saveAll(List.of(adminRole, communityManagerRole, tipperRole));
        
        adminRole.addPrivileges(createUpdateDeleteReadSeason);

        privilegeRepository.saveAll(
                List.of(createCommunity, updateCommunity, deleteCommunity, readCommunity));
        
        adminRole.addPrivilege(createSeason, updateSeason, deleteSeason);
        adminRole.addPrivilege(createRound, updateRound, deleteRound, readRound);
        adminRole.addPrivilege(createGame, updateGame, deleteGame, readGame);
        adminRole.addPrivilege(createCommunity, updateCommunity, deleteCommunity, readCommunity);

        communityManagerRole.addPrivilege(updateCommunity, readCommunity);

        tipperRole.addPrivilege(readSeason, readRound, readGame, readTipp, createTipp, updateTipp, deleteTipp, readCommunity);

        roleRepository.save(adminRole);
        roleRepository.save(communityManagerRole);
        roleRepository.save(tipperRole);

        UserAccountEntity tipper = of("Frosch", "secret-Password")
                .firstname("Tipp")
                .name("King")
                .created(LocalDateTime.now())
                .email(Email.of("test@testmail.com"))
                .build();
        UserAccountEntity communityManager = of("CommuniyManager", "another-secret")
                .firstname("Community")
                .name("Manager")
                .created(LocalDateTime.now())
                .email(Email.of("test@testmail.com"))
                .build();
        UserAccountEntity admin = of("admin", "super-secret-admin-password")
                .firstname("admin")
                .name("admin")
                .email(Email.of("test@testmail.com"))
                .created(LocalDateTime.now())
                .build();

        tipper.addRole(tipperRole);
        communityManager.addRole(communityManagerRole);
        admin.addRole(adminRole);

        userAccountRepository.saveAll(List.of(tipper, communityManager, admin));
    }

    @DisplayName("Repository test: Find all users")
    @Test
    @Tag(Tags.REPOSITORY)
    public void userRolePrivilegeRelation() {
        Iterable<PrivilegeEntity> privileges = privilegeRepository.findAll();
        assertThat(privileges).isNotNull().hasSize(19);

        RoleEntity tipperRole = roleRepository.findByName("Tipper").orElseThrow();
        UserAccountEntity frosch = userAccountRepository.findByNickname("Frosch").orElseThrow();
        assertThat(frosch.hasRole(tipperRole)).isTrue();
    }

}
