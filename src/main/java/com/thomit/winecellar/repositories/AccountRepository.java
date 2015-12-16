package com.thomit.winecellar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomit.winecellar.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountId(String accountId);
}
