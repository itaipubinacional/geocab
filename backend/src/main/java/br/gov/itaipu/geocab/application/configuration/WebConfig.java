package br.gov.itaipu.geocab.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by lcvmelo on 23/02/2017.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ResourceProperties resourceProperties = new ResourceProperties();

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
         * O Spring precisa dessa lógica para suportar as rotas do HTML5 usadas
         * pelo Angular.
         */
        Integer cachePeriod = resourceProperties.getCachePeriod();

        registry.addResourceHandler(
                "/**/*.css",
                "/**/*.html",
                "/**/*.js",
                "/**/*.json",
                "/**/*.bmp",
                "/**/*.jpeg",
                "/**/*.jpg",
                "/**/*.png",
                "/**/*.ttf",
                "/**/*.eot",
                "/**/*.svg",
                "/**/*.woff",
                "/**/*.woff2")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(cachePeriod);

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/index.html")
                .setCachePeriod(cachePeriod)
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath,
                                                   Resource location) throws IOException {
                        return location.exists() && location.isReadable() ? location
                                : null;
                    }
                });
    }

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
}
