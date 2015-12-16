package com.thomit.winecellar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import com.thomit.winecellar.repositories.AccountRepository;
import com.thomit.winecellar.exceptions.ClientNotFoundException;

@Configuration
@EnableAuthorizationServer
public class WineCellarAuthorizationServerConfiguration extends
		AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security)
			throws Exception {
		security.passwordEncoder(passwordEncoder);
	}

	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		endpoints.authenticationManager(authenticationManager);
		
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		clients.withClientDetails(getClientDetailsService());
	}
		
	@Bean
	public ClientDetailsService getClientDetailsService() {
		return (clientId) -> accountRepository.findByAccountId(clientId)
				.map(a ->{
					BaseClientDetails baseClientDetails = new BaseClientDetails(a.getAccountId(), a.getResourceIds(), a.getScopes(), a.getGrantTypes(), a.getAuthorities(), a.getRedirectUris());
					baseClientDetails.setClientSecret(a.getAccountSecret());
					return baseClientDetails;
				})
				.orElseThrow(
						() -> new ClientNotFoundException(clientId)
				);
		}
	
}
