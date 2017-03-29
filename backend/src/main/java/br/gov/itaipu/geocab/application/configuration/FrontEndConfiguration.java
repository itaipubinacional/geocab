package br.gov.itaipu.geocab.application.configuration;

import br.gov.itaipu.geocab.application.security.AuthConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Classe que representa as configurações do sistema que deverão ser compartilhadas
 * entre o back-end e o front-end.
 */
@Component
public class FrontEndConfiguration {

    @Autowired
    @Qualifier("authConfiguration")
    private AuthConfiguration auth;

    public AuthConfiguration getAuth() {
        return auth;
    }

    public void setAuth(AuthConfiguration auth) {
        this.auth = auth;
    }
}
