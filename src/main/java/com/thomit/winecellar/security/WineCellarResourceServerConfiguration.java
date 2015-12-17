package com.thomit.winecellar.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@Order(2)
@EnableResourceServer
public class WineCellarResourceServerConfiguration extends
		ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
				.authorizeRequests()
				.antMatchers("/user/**").access("#oauth2.clientHasRole('CLIENT_MOBILE_APP') and !hasRole('ROLE_USER') ")
				.and()
				.authorizeRequests()
				.antMatchers("/wine/**").access("#oauth2.clientHasRole('CLIENT_MOBILE_APP') and hasRole('ROLE_USER')");
		}

}
//#oauth2.isClient()
