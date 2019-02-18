/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sweater.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author a.davydov
 */
@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;
    
    @Value("${spring.mail.username}")
    private String username;
    
    @Value("${spring.mail.password}")
    private String password;
    
    @Value("${spring.mail.port}")
    private int port;
    
    @Value("${spring.mail.protocol}")
    private String protocol;
    
    @Value("${mail.debug}")
    private String debug;
    
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;
    
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String enable;
    
    @Value("${mail.smtp.ssl.enable}")
    private String sslEnable;
    
    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties properties = mailSender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.debug", debug);
        properties.setProperty("mail.smtp.auth", auth);
        properties.setProperty("mail.smtp.starttls.enable", enable);  
        //properties.setProperty("mail.smtp.socketFactory.port", "465");
        //properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //properties.setProperty("mail.smtp.ssl.enable", "true");
        
        return mailSender;
    }
}
