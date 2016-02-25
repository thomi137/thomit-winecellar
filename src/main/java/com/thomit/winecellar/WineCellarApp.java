package com.thomit.winecellar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.thomit.winecellar.utils.SecurityEvaluationContextExtension;

@SpringBootApplication
@EnableJpaAuditing
@EnableAuthorizationServer
@EnableResourceServer
@EnableHypermediaSupport(type = { HypermediaType.HAL })
public class WineCellarApp {

    public static void main(String[] args) {
        SpringApplication.run(WineCellarApp.class, args);
    }
    
    @Bean
    PasswordEncoder passwordEncoder(){
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    RelProvider getRelProvider(){
    	return new EvoInflectorRelProvider();
    }
    
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension(){
        return new SecurityEvaluationContextExtension();
    }
 
}
