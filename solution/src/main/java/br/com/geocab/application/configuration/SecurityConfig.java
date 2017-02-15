package br.com.geocab.application.configuration;

import br.com.geocab.application.security.CorsFilter;
import br.com.geocab.application.security.SocialAuthenticationEntryPoint;
import br.com.geocab.application.security.SocialAuthenticationFilter;
import br.com.geocab.domain.repository.account.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by lcvmelo on 13/02/2017.
 */
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@PropertySource("classpath:geocab.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Value("${security.blowfish}")
    private String salt;

    @Autowired
    private IUserRepository userRepository;

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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userRepository);
        provider.setPasswordEncoder(passwordEncoder());

        provider.setSaltSource(saltSource());

        // ajusta o serviço do sistema que retornará os detalhes do usuário logado
        auth.authenticationProvider(provider);
    }

    /*
     * Na configuração original existiam diferentes seções de regras de segurança para a API REST
     * e para a autenticação via form. Para fazer algo semelhante em Java foi necessário criar
     * classes aninhadas que definem cada uma destas seções.
     * Referência:
     * - http://stackoverflow.com/questions/18815015/creating-multiple-http-sections-in-spring-security-java-config
     */

    @Configuration
    public static class NoneWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter
    {
        @Override
        public void configure(WebSecurity web) throws Exception
        {
        /*
         * Essa lógica é equivalente a utilizar o http com security=none no
         * arquivo XML.
         */
            web.ignoring()
                    .antMatchers(
                            // ignored URLs
                            "/favicon.ico*",
                            "/signin/**",
                            "/signup/**",

                            // static files
                            "/static/**",
                            "/webjars/**",
                            "/broker/engine.js",
                            "/broker/util.js",

                            // mobile authentication
                            "/modules/authentication/**",

                            // login service
                            "/broker/call/plaincall/loginService.*",
                            "/broker/call/plaincall/__System.*",
                            "/broker/interface/loginService.js",

                            // translator
                            "/bundles");
        }
    }

    @Configuration
    public static class ApiRestWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter
    {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            SocialAuthenticationEntryPoint entryPoint = new SocialAuthenticationEntryPoint();

            http.httpBasic().authenticationEntryPoint(entryPoint);
            // adiciona o filtro do CORS
            http.addFilterAfter(new CorsFilter(), AbstractPreAuthenticatedProcessingFilter.class);
            // TODO tem uma classe do próprio Spring deste filtro. Checar
            // adiciona o filtro do login com rede sociais
            http.addFilterBefore(
                    new SocialAuthenticationFilter(
                            this.authenticationManager, entryPoint),
                    BasicAuthenticationFilter.class);
            // configuração de segurança para a API REST
            http.authorizeRequests()
                    .antMatchers(
                            "/api/authentication/**",
                            "/api/marker/**",
                            "/api/layergroup/**",
                            "/api/files/**"
                    ).authenticated();
        }
    }

    @Configuration
    public static class FormLoginWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter
    {
        @Bean
        public AuthenticationSuccessHandler authenticationSuccessHandler()
        {
            SavedRequestAwareAuthenticationSuccessHandler handler =
                    new SavedRequestAwareAuthenticationSuccessHandler();
            handler.setUseReferer(true);
            return handler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception
        {
            http.authorizeRequests()
                    .antMatchers(
                            "/admin/**",
                            "/users/**")
                    .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(authenticationSuccessHandler())
                    .permitAll();
        }
    }
}
