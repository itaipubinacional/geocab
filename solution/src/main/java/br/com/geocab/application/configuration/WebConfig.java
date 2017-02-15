package br.com.geocab.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.Properties;

/**
 * Created by lcvmelo on 13/02/2017.
 */
@Configuration
@EnableWebMvc
@ComponentScan({
        "br.com.geocab.application.controller",
        "br.com.geocab.application.restful",
        "br.com.geocab.infrastructure.social.mobile"
})
@ImportResource("/WEB-INF/spring/dwr-context.xml")
public class WebConfig extends WebMvcConfigurerAdapter
{
    @Bean
    public VelocityEngineFactoryBean velocityEngine() {
        VelocityEngineFactoryBean factoryBean = new VelocityEngineFactoryBean();

        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        factoryBean.setVelocityProperties(properties);
        return factoryBean;
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/modules/**")
                .addResourceLocations("/modules/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("locale");

        // TODO Checar isso
        //OpenEntityManagerInViewInterceptor inViewInterceptor =
        //                new OpenEntityManagerInViewInterceptor();

        registry.addInterceptor(localeChangeInterceptor);
        //registry.addInterceptor(inViewInterceptor);
    }


}
