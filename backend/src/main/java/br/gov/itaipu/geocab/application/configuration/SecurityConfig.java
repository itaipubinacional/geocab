package br.gov.itaipu.geocab.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by lcvmelo on 23/02/2017.
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.blowfish}")
    private String salt;

    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder();
    }

    @Bean
    public SaltSource saltSource() {
        SystemWideSaltSource saltSource = new SystemWideSaltSource();
        saltSource.setSystemWideSalt(this.salt);
        return saltSource;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*
         * Habilita tudo já que todo serviço REST terá a sua lógica de verificação
         */
        http
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .csrf().disable();
    }
}
