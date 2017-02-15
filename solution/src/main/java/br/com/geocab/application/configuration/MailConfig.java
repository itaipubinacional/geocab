package br.com.geocab.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by lcvmelo on 14/02/2017.
 */
@Configuration
@ComponentScan("br.com.geocab.infrastructure.mail")
@PropertySource("classpath:mail.properties")
public class MailConfig
{
    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.tls}")
    private boolean tlsEnabled;

    @Value("${mail.tls}")
    private boolean tlsRequired;

    @Value("${mail.ssl}")
    private boolean sslEnabled;

    @Value("${mail.debug}")
    private boolean smtpsDebug;

    @Value("${mail.debug}")
    private boolean smtpDebug;

    @Value("${mail.debug}")
    private boolean debug;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setProtocol(this.protocol);
        sender.setHost(this.host);
        sender.setPort(this.port);

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", String.valueOf(this.tlsEnabled));
        properties.setProperty("mail.smtp.starttls.required", String.valueOf(this.tlsRequired));
        properties.setProperty("mail.smtp.ssl.enable", String.valueOf(this.sslEnabled));
        properties.setProperty("mail.smtps.debug", String.valueOf(this.smtpsDebug));
        properties.setProperty("mail.smtp.debug", String.valueOf(this.smtpDebug));
        properties.setProperty("mail.debug", String.valueOf(this.debug));
        sender.setJavaMailProperties(properties);

        return sender;
    }
}
