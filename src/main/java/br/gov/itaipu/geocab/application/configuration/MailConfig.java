package br.gov.itaipu.geocab.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by lcvmelo on 23/02/2017.
 */
@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Configuration
    public static class Server {

        @Value("${mail.protocol}")
        public String protocol;

        @Value("${mail.host}")
        public String host;

        @Value("${mail.port}")
        public int port;

        @Value("${mail.tls}")
        public boolean tlsEnabled;

        @Value("${mail.tls}")
        public boolean tlsRequired;

        @Value("${mail.ssl}")
        public boolean sslEnabled;

        @Value("${mail.debug}")
        public boolean smtpsDebug;

        @Value("${mail.debug}")
        public boolean smtpDebug;

        @Value("${mail.debug}")
        public boolean debug;

        @Value("${mail.from}")
        public String from;
    }

    @Bean
    public JavaMailSender mailSender(Server serverConfig) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setProtocol(serverConfig.protocol);
        sender.setHost(serverConfig.host);
        sender.setPort(serverConfig.port);

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", String.valueOf(serverConfig.tlsEnabled));
        properties.setProperty("mail.smtp.starttls.required", String.valueOf(serverConfig.tlsRequired));
        properties.setProperty("mail.smtp.ssl.enable", String.valueOf(serverConfig.sslEnabled));
        properties.setProperty("mail.smtps.debug", String.valueOf(serverConfig.smtpsDebug));
        properties.setProperty("mail.smtp.debug", String.valueOf(serverConfig.smtpDebug));
        properties.setProperty("mail.debug", String.valueOf(serverConfig.debug));
        sender.setJavaMailProperties(properties);

        return sender;
    }
}
