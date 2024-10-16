package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class ServerConfiguration {

    @Bean
    public DispatcherServlet customDispatcherServlet() {
        return new CustomDispatcherServlet();
    }
}
