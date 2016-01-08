package co.lsnbox.auth;

import co.lsnbox.auth.configs.security.MongoDBTokenStore;
import co.lsnbox.auth.models.Role;
import co.lsnbox.auth.repositories.OAuth2AccessTokenRepository;
import co.lsnbox.auth.repositories.OAuth2RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Filip Lindby
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {


    @Value("${authentication.oauth.clientid}")
    public String PROP_CLIENTID;
    @Value("${authentication.oauth.secret}")
    public String PROP_SECRET;
    @Value("${authentication.oauth.tokenValidityInSeconds}")
    public Integer PROP_TOKEN_VALIDITY_SECONDS;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OAuth2AccessTokenRepository oAuth2AccessTokenRepository;

    @Autowired
    private OAuth2RefreshTokenRepository oAuth2RefreshTokenRepository;

    @Bean
    public TokenStore tokenStore() {
        return new MongoDBTokenStore(oAuth2AccessTokenRepository, oAuth2RefreshTokenRepository);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(this.authenticationManager)
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(PROP_CLIENTID)
                .scopes("read", "write")
                .autoApprove(true)
                .authorities(Role.ADMIN.name(), Role.USER.name())
                .authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_credentials")
                .secret(PROP_SECRET)
                .accessTokenValiditySeconds(PROP_TOKEN_VALIDITY_SECONDS);
    }

}