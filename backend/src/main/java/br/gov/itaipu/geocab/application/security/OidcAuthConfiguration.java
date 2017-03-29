package br.gov.itaipu.geocab.application.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Classe que representa as configurações dos serviços de autenticação dos usuários do
 * sistema utilizando o OpenID Connect.
 */
@Component("oidcAuth")
public class OidcAuthConfiguration extends AuthConfiguration {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUrl;

    @Value("${security.oauth2.client.user-authorization-uri}")
    private String userAuthorizationUrl;

    @Value("${security.oauth2.resource.user-info-uri}")
    private String userInfoUrl;

    @Value("${oauth2.log-out-uri}")
    private String logoutUrl;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public String getUserAuthorizationUrl() {
        return userAuthorizationUrl;
    }

    public void setUserAuthorizationUrl(String userAuthorizationUrl) {
        this.userAuthorizationUrl = userAuthorizationUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public void setUserInfoUrl(String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
}
