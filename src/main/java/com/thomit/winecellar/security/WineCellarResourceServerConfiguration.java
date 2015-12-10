package com.thomit.winecellar.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@Order(2)
public class WineCellarResourceServerConfiguration extends
		ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
				.requestMatchers()
				.antMatchers("/oauth/users/**", "/oauth/clients/**").and()
				.authorizeRequests().anyRequest().authenticated().and()
				.antMatcher("/api/**").authorizeRequests().anyRequest()
				.authenticated();
	}

}
