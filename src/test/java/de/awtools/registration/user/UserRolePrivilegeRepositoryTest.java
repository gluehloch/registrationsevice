package de.awtools.registration.user;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class UserRolePrivilegeRepositoryTest {

    PrivilegeRepository privilegeRepository;
    
    @DisplayName("Repository test: Find all users")
    @Test
    @Tag(Tags.REPOSITORY)
    public void userRolePrvilegeRelation() {
        PrivilegeEntity privilege = null;
        // TODO 
        
    }

}
