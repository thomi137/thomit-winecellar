package com.thomit.winecellar.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thomit.winecellar.models.Account;
import com.thomit.winecellar.models.Wine;
import com.thomit.winecellar.repositories.AccountRepository;
import com.thomit.winecellar.repositories.WineRepository;
import com.thomit.winecellar.utils.WineResource;

@RestController
@RequestMapping("/wine/{userId}/wines")
@ExposesResourceFor(Wine.class)
public class WineController {

	private final WineRepository wineRepository;
	
	private final AccountRepository accountRepository;
	
	@PreAuthorize("hasRole('ROLE_USER') and #userId == authentication.name")
	@RequestMapping(value = "/{wineId}", method = RequestMethod.GET)
	public WineResource readWine(@PathVariable Long wineId, @PathVariable String userId){
		return new WineResource(this.wineRepository.findOne(wineId));
	}
	
	@PreAuthorize("hasRole('ROLE_USER') and #userId == authentication.name")
	@RequestMapping(method = RequestMethod.GET)
	Resources<WineResource> readWines(@PathVariable String userId){
		List<WineResource> wineResourceList = wineRepository.findByAccountAccountId(userId)
				.stream()
				.map(WineResource::new)
				.collect(Collectors.toList());
		return new Resources<WineResource>(wineResourceList);
	}
	
	@PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_USER') and #userId == authentication.name")
	@RequestMapping(method = RequestMethod.POST)
	ResponseEntity<?> addWine(@PathVariable String userId, @RequestBody Wine newWine){
		
		Account userAccount = accountRepository.findByAccountId(userId).get();
		newWine.setAccount(userAccount);
		
		Wine addedWine = wineRepository.save(newWine);
		HttpHeaders httpHeaders = new HttpHeaders();
		
		Link forOneWine = new WineResource(addedWine).getLink("self");
		httpHeaders.setLocation(URI.create(forOneWine.getHref()));
		
		return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
	}

	@Autowired
	public WineController(WineRepository wineRepository, AccountRepository accountRepository){
		this.wineRepository = wineRepository;
		this.accountRepository = accountRepository;
	}
	
}
