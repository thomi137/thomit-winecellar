package com.thomit.winecellar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.thomit.winecellar.models.Account;

@RestResource(exported=false)
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountId(String accountId);
}
