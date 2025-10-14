package com.dev.turismo.agencia.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<SessionAuthFilter> sessionFilter(SessionAuthFilter f) {
        var reg = new FilterRegistrationBean<SessionAuthFilter>();
        reg.setFilter(f);
        reg.addUrlPatterns("/api/*"); // só nas APIs
        reg.setOrder(1);
        return reg;
    }
}