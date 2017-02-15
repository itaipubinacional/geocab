package br.com.geocab.application.configuration;

import br.com.geocab.application.aspect.ExceptionHandlerAspect;
import br.com.geocab.infrastructure.jcr.MetaFileRepository;
import br.com.geocab.infrastructure.jcr.modeshape.ModeShapeRepositoryFactory;
import br.com.geocab.infrastructure.jcr.modeshape.ModeShapeSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.jcr.RepositoryException;
import javax.validation.ValidatorFactory;
import java.util.Locale;

/**
 * Created by lcvmelo on 13/02/2017.
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({
        "br.com.geocab.infrastructure.file",
        "br.com.geocab.infrastructure.jcr"}) // TODO mudar isso depois
public class ApplicationConfig implements ApplicationContextAware
{
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.context = applicationContext;
    }

    /**
     * Bean utilizado para resolver as propriedades anotadas com a anotação
     * {@link Value} do Spring.
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Bean que contém as mensagens intercionalizadas do sistema.
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // TODO Revisar esses essas mensagens
        messageSource.setBasenames(
                "classpath:i18n/admin",
                "classpath:i18n/map",
                "classpath:i18n/exceptions");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(false);

        return messageSource;
    }

    /**
     * Bean do aspecto que trata as exceções que são lançadas pelo sistema durante
     * as chamadas de serviços.
     */
    @Bean
    public ExceptionHandlerAspect exceptionHandlerAspect() {
        return new ExceptionHandlerAspect();
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ModeShapeRepositoryFactory modeShapeRepositoryFactory() {
        ModeShapeRepositoryFactory factory = new ModeShapeRepositoryFactory();
        factory.setConfiguration(context.getResource("classpath:jcr-modeshape-config.json"));
        return factory;
    }

    @Bean
    public ModeShapeSessionFactory modeShapeSessionFactory() {
        return new ModeShapeSessionFactory(modeShapeRepositoryFactory().getObject());
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("pt_BR"));
        resolver.setCookieMaxAge(604800);
        return resolver;
    }
}
