package com.thomit.winecellar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@EnableGlobalAuthentication
public class WineCellarSecurityConfiguration extends
		WebSecurityConfigurerAdapter {

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication().withUser("wine").password("secret").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
		
		
		http.formLogin().permitAll()
			.and().requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
			.and().authorizeRequests().anyRequest().authenticated();
		 
	
	}
	
}
