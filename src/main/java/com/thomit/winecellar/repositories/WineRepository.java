package com.thomit.winecellar.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.thomit.winecellar.models.Wine;

@RepositoryRestResource(collectionResourceRel="wine", path="/wine")
public interface WineRepository extends JpaRepository<Wine, Long> {

	List<Wine> findByName(@Param("name") String name);
	List<Wine> findByAccountAccountId(@Param("accountId") String accountId);
	
	Optional<Wine> findByIdAndAccountAccountId(Long id, String accountId);
	
}
