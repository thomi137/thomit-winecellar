package com.thomit.winecellar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
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
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		endpoints.authenticationManager(authenticationManager);
	}
		
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		
		clients.inMemory()
			.withClient("wine")
			.secret("secret")
			.authorizedGrantTypes("client_credentials", "refresh_token", "password")
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
