package br.com.geocab.application.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Created by lcvmelo on 15/02/2017.
 */
public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        return new Class<?>[] {
                ApplicationConfig.class,
                DbConfig.class,
                MailConfig.class,
                SocialConfig.class,
                ServicesConfig.class,
                WebConfig.class,
                SecurityConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        return null;
    }

    @Override
    protected String[] getServletMappings()
    {
        return new String[] {
                "/"
        };
    }


}
