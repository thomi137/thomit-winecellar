package com.thomit.winecellar.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thomit.winecellar.models.Account;
import com.thomit.winecellar.models.WineUser;
import com.thomit.winecellar.repositories.AccountRepository;

@RestController
@EnableResourceServer
@RequestMapping("/user")
public class UserController {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('read')")
	public Principal getUser(Principal user) {
		return user;
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("#oauth2.hasScope('write')")
	ResponseEntity<?> addUser(Principal principal, @RequestBody WineUser user) throws Exception {

		Account newAccount = new Account(user.getUsername(),
				passwordEncoder.encode(user.getPassword()), null, "read,write",
				null, "ROLE_USER");
		accountRepository.save(newAccount);

		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@RequestMapping(method = RequestMethod.DELETE)
	@PreAuthorize("#oauth2.hasScope('write')")
	ResponseEntity<?> deleteUser(Principal principal, @RequestBody WineUser user) {

		accountRepository.delete(accountRepository
				.findByAccountId(user.getUsername()).get().getId());

		return new ResponseEntity<>(HttpStatus.OK);

	}

}
