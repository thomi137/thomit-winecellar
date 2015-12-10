package com.thomit.winecellar.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class WineCellarAuthorizationServerConfiguration extends
		AuthorizationServerConfigurerAdapter {

	/*
	@Override
	public void configure(security)
			throws Exception {
		security.passwordEncoder(passwordEncoder);
	}
	*/
	
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		
		clients.inMemory()
			.withClient("wine")
			.secret("secret")
			.authorizedGrantTypes("client_credentials")
			.scopes("read", "write");
		
		// clients.withClientDetails(getClientDetailsService());
	}

	/*
	
	
	@Bean
	public ClientDetailsService getClientDetailsService() {
		return new VZClientDetailsService();
	}
	
	*/

}
