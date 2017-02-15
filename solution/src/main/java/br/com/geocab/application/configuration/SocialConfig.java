package br.com.geocab.application.configuration;

import br.com.geocab.infrastructure.social.ConnectionSignUp;
import br.com.geocab.infrastructure.social.PersistentUsersConnectionRepository;
import br.com.geocab.infrastructure.social.SpringSecuritySignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by lcvmelo on 10/02/2017.
 */
@Configuration
@PropertySource("classpath:geocab.properties")
public class SocialConfig
{
    @Autowired
    private Environment environment;

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

        // configurações para login com o Google
        GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(
                this.environment.getProperty("social.google.app-id"),
                this.environment.getProperty("social.google.app-secret")
        );

        // configurações para login com o Facebook
        FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(
                this.environment.getProperty("social.facebook.app-id"),
                this.environment.getProperty("social.facebook.app-secret")
        );

        registry.addConnectionFactory(googleConnectionFactory);
        registry.addConnectionFactory(facebookConnectionFactory);
        return registry;
    }

    @Bean
    public UsersConnectionRepository usersConnectionRepository() {
        PersistentUsersConnectionRepository repository =
                new PersistentUsersConnectionRepository();
        repository.setConnectionSignUp(new ConnectionSignUp());
        return repository;
    }

    @Bean
    @Scope(value="request", proxyMode = ScopedProxyMode.NO)
    public ConnectionRepository connectionRepository(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return usersConnectionRepository().createConnectionRepository(principal.getName());
    }

    @Bean
    public UserIdSource userIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Bean
    public SignInAdapter signInAdapter() {
        return new SpringSecuritySignInAdapter();
    }

    @Bean
    public ProviderSignInController providerSignInController() {
        return new ProviderSignInController(
                connectionFactoryLocator(),
                usersConnectionRepository(),
                signInAdapter());
    }
}
