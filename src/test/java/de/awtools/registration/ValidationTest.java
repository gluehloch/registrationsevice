package de.awtools.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@Rollback
public class ValidationTest {

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
                .perform(get("/registration/ping")
                        .header("Content-type", "application/json")
                        .header("charset", "UTF-8"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentType())
                .isEqualTo("application/json;charset=utf-8");
    }

    @Test
    public void validateUnknownApplication() throws Exception {
        ServletContext context = webAppContext.getServletContext();

        RegistrationJson registration = new RegistrationJson();
        registration.setApplicationName("unknownApplication");
        registration.setEmail("email@web.de");
        registration.setFirstname("firstname");
        registration.setName("name");
        registration.setNickname("nickname");
        registration.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(registration);

        MvcResult result = mockMvc
                .perform(post("/registration/validate")
                        .content(json)
                        .header("Content-type", "application/json")
                        .header("charset", "UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("validationCode").value("1003"))
                // .andExpect(content().json("{'validationCode': 1003}"))
                .andReturn();

        assertThat(result.getResponse().getContentType())
                .isEqualTo("application/json;charset=utf-8");

        // RegistrationValidation validation = mapper.readValue(
        // result.getResponse().getContentAsString(),
        // RegistrationValidation.class);
        //
        // assertThat(validation.getValidationCode())
        // .isEqualTo(ValidationCode.ILLEGAL_ARGUMENTS);
    }

}
