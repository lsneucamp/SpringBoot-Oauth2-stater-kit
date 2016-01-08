package co.lsnbox.auth;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by luciano on 23/02/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AuthserverApplication.class)
@WebAppConfiguration
public abstract  class AbstractTest {
    @Inject
    protected UserDetailsService userDetailsService;

    @Value("${authentication.oauth.clientid}")
    public String PROP_CLIENTID;
    @Value("${authentication.oauth.secret}")
    public String PROP_SECRET;
    @Value("${authentication.oauth.tokenValidityInSeconds}")
    public Integer PROP_TOKEN_VALIDITY_SECONDS;


    @Inject
    private WebApplicationContext webApplicationContext;

    @Inject
    private FilterChainProxy springSecurityFilterChain;

    protected MockMvc mvc;

    protected ObjectMapper mapper;
    private MockHttpSession mockSession;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mvc = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
        mockSession = new MockHttpSession(webApplicationContext.getServletContext(), UUID.randomUUID().toString());
    }


    protected String getAccessToken(String username, String password) throws Exception {


        String authorization = "Basic " + new String(Base64Utils.encode((PROP_CLIENTID + ":" + PROP_SECRET).getBytes()));
        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";
        String content = mvc
                .perform(
                        post("/oauth/token")
                                .header("Authorization", authorization)
//                        .contentType(
//                                MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", username)
                                .param("password", password)
                                .param("grant_type", "password")
                                .param("scope", "read write")
                                .param("client_id", PROP_CLIENTID)
                                .param("client_secret", PROP_SECRET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                        // .andExpect(jsonPath("$.expires_in", is(greaterThan(new Double(PROP_TOKEN_VALIDITY_SECONDS*0.9).intValue()))))
                .andExpect(jsonPath("$.scope", is(equalTo("read write"))))
                .andReturn().getResponse().getContentAsString();

        return content.substring(17, 53);
    }




}
