package de.awtools.registration.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import de.awtools.registration.Tags;
import de.awtools.registration.config.PersistenceJPAConfig;
import de.awtools.registration.user.ApplicationEntity.ApplicationBuilder;
import de.awtools.registration.user.PrivilegeEntity.PrivilegeBuilder;
import de.awtools.registration.user.RoleEntity.RoleBuilder;
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

        privilegeRepository.saveAll(
                List.of(createTipp, updateTipp, deleteTipp, readTipp));

        PrivilegeEntity createCommunity = PrivilegeBuilder.of("CREATE community");
        PrivilegeEntity updateCommunity = PrivilegeBuilder.of("UPDATE community");
        PrivilegeEntity deleteCommunity = PrivilegeBuilder.of("DELETE community");
        PrivilegeEntity readCommunity = PrivilegeBuilder.of("READ community");

        privilegeRepository.saveAll(
                List.of(createCommunity, updateCommunity, deleteCommunity, readCommunity));

        RoleEntity admin = RoleBuilder.of("ADMIN");
        RoleEntity communityManager = RoleBuilder.of("Community Manager");
        RoleEntity tipper = RoleBuilder.of("Tipper");

        roleRepository.saveAll(List.of(admin, communityManager, tipper));
        
        admin.addPrivilege(createSeason, updateSeason, deleteSeason);
        admin.addPrivilege(createRound, updateRound, deleteRound, readRound);
        admin.addPrivilege(createGame, updateGame, deleteGame, readGame);
        admin.addPrivilege(createCommunity, updateCommunity, deleteCommunity, readCommunity);

        communityManager.addPrivilege(updateCommunity, readCommunity);

        tipper.addPrivilege(readSeason, readRound, readGame, readTipp, createTipp, updateTipp, deleteTipp, readCommunity);

        roleRepository.save(admin);
        roleRepository.save(communityManager);
        roleRepository.save(tipper);
    }

    @DisplayName("Repository test: Find all users")
    @Test
    @Tag(Tags.REPOSITORY)
    public void userRolePrvilegeRelation() {
        Iterable<PrivilegeEntity> privileges = privilegeRepository.findAll();
        assertThat(privileges).isNotNull();
        assertThat(privileges).hasSize(19);
        // assertThat(privileges).contains(readPriv, writePriv, deletePriv);
    }

}
