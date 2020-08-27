package de.awtools.registration.cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import de.awtools.registration.config.PersistenceJPAConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
@ComponentScan("de.awtools.registration")
@WebAppConfiguration
public class CookieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CookieService cookieService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CookieController(cookieService))
                .setHandlerExceptionResolvers(new MyExceptionResolver())
                .build();
    }

    @Test
    public void cookies() throws Exception {
        mockMvc.perform(get("/cookie/ping")).andDo(print()).andExpect(status().isOk());
    }
    
    private static class MyExceptionResolver implements HandlerExceptionResolver {

        @Override
        public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                Exception ex) {

            return null;
        }
        
    }
    
}
