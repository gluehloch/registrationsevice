package de.awtools.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.ServletContext;

import de.awtools.registration.register.RegistrationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.awtools.registration.config.PersistenceJPAConfig;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@Rollback
public class WebMvcTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext)
                .build();
    }

    @Test
    public void ping() throws Exception {
        ServletContext context = webAppContext.getServletContext();

        assertThat(context).isNotNull();
        assertThat(context).isInstanceOf(MockServletContext.class);
        assertThat(context.getServletContextName())
                .isEqualTo("MockServletContext");
        assertThat(webAppContext.getBean(RegistrationController.class))
                .isNotNull();

        MvcResult result = mockMvc
                .perform(get("/ping")
                        .header(HttpHeaders.CONTENT_TYPE,"application/json")
                        .header(HttpHeaders.ACCEPT_CHARSET, "charset", "UTF-8"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentType())
                .isEqualTo("application/json;charset=utf-8");
    }

}
