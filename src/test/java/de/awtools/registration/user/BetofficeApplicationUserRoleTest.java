package de.awtools.registration.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    public void before() {
        ApplicationEntity betofficeApplication = ApplicationBuilder.of("betoffice").description("Betoffice Community");

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

        PrivilegeEntity createTipp = PrivilegeBuilder.of("CREATE tipp");
        PrivilegeEntity updateTipp = PrivilegeBuilder.of("UPDATE tipp");
        PrivilegeEntity deleteTipp = PrivilegeBuilder.of("DELETE tipp");
        PrivilegeEntity readTipp = PrivilegeBuilder.of("READ tipp");

        List<PrivilegeEntity> createUpdateDeleteReadSeason =
            List.of(createSeason, updateSeason, deleteSeason,
                createRound, updateRound, deleteRound, readRound,
                createGame, updateGame, deleteGame, readGame);

        List<PrivilegeEntity> createUpdateDeleteReadTipp =
            List.of(createTipp, updateTipp, deleteTipp, readTipp);

        RoleEntity admin = RoleBuilder.of("ADMIN");
        RoleEntity communityManager = RoleBuilder.of("Community Manager");
        RoleEntity tipper = RoleBuilder.of("Tipper");

        applicationRepository.save(betofficeApplication);
    }

    @DisplayName("Repository test: Find all users")
    @Test
    @Tag(Tags.REPOSITORY)
    public void userRolePrvilegeRelation() {
        PrivilegeEntity readPriv = PrivilegeEntity.PrivilegeBuilder.of("READ");
        PrivilegeEntity writePriv = PrivilegeEntity.PrivilegeBuilder.of("WRITE");
        PrivilegeEntity deletePriv = PrivilegeEntity.PrivilegeBuilder.of("DELETE");

        privilegeRepository.saveAll(Set.of(readPriv, writePriv, deletePriv));

        Iterable<PrivilegeEntity> privileges = privilegeRepository.findAll();
        assertThat(privileges).isNotNull();
        assertThat(privileges).hasSize(3);
        assertThat(privileges).contains(readPriv, writePriv, deletePriv);
    }

}
