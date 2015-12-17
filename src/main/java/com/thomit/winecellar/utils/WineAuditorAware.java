package com.thomit.winecellar.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.thomit.winecellar.models.Account;
import com.thomit.winecellar.repositories.AccountRepository;

@Component
public class WineAuditorAware implements AuditorAware<Account> {

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public Account getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()){
			return null;
		}
		
		return accountRepository.findByAccountId(authentication.getName()).get();
	}
	
}
