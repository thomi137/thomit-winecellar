package com.thomit.winecellar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thomit.winecellar.repositories.AccountRepository;
import com.thomit.winecellar.exceptions.UsernameNotFoundException;

@EnableWebSecurity
public class WineCellarSecurityConfiguration extends
		WebSecurityConfigurerAdapter {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(
				passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();

		http.formLogin()
				.permitAll()
				.and()
				.requestMatchers()
				.antMatchers("/login", "/oauth/authorize",
						"/oauth/confirm_access").and().authorizeRequests()
				.anyRequest().authenticated();

	}

	@Bean
	protected UserDetailsService userDetailsService() {
		return (username) -> accountRepository
				.findByAccountId(username)
				.map(a -> new User(a.getAccountId(), a.getAccountSecret(),
						true, true, true, true, AuthorityUtils
								.commaSeparatedStringToAuthorityList(a
										.getAuthorities())))
				.orElseThrow(() -> new UsernameNotFoundException(username));
	}

}
