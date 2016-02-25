package com.thomit.winecellar.test.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("test")
public class TestSecurityConfiguration extends
		WebSecurityConfigurerAdapter {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication().withUser("test-user").password("test-user-secret").authorities("ROLE_USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.formLogin()
				.permitAll()
				.and()
				.requestMatchers()
				.antMatchers("/login", "/oauth/authorize",
						"/oauth/confirm_access").and().authorizeRequests()
				.anyRequest().authenticated();

	}

}
