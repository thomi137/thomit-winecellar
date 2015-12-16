package com.thomit.winecellar.controllers;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableResourceServer
public class UserController {
	
	@RequestMapping(path = "/user", method=RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('read')")
	public Principal getUser(Principal user){
		return user;
	}
	
}
