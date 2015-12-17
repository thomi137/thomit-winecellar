package com.thomit.winecellar.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "accounts")
@RestResource(exported = false)
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@JsonIgnore
	String accountId;
	
	@JsonIgnore
	String accountSecret;
	
	@JsonIgnore
	String resourceIds;
	
	@JsonIgnore
	String scopes;
	
	@JsonIgnore
	String grantTypes;
	
	@JsonIgnore
	String redirectUris;
	
	@JsonIgnore
	String authorities;
	
	public Account(){
		
	}

	public Account(String clientId, String clientSecret, String resourceIds,
			String scopes, String grantTypes, String redirectUris, String authorities) {
		this.accountId = clientId;
		this.accountSecret = clientSecret;
		this.resourceIds = resourceIds;
		this.scopes = scopes;
		this.grantTypes = grantTypes;
		this.authorities = authorities;
	}
	
	public Account(String clientId, String clientSecret, String resourceIds,
			String scopes, String grantTypes, String authorities) {
		this(clientId, clientSecret, resourceIds, scopes, grantTypes, "", authorities);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountSecret() {
		return accountSecret;
	}

	public void setAccountSecret(String accountSecret) {
		this.accountSecret = accountSecret;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getScopes() {
		return scopes;
	}

	public void setScopes(String scopes) {
		this.scopes = scopes;
	}

	public String getGrantTypes() {
		return grantTypes;
	}

	public void setGrantTypes(String grantTypes) {
		this.grantTypes = grantTypes;
	}

	public String getRedirectUris() {
		return redirectUris;
	}

	public void setRedirectUris(String redirectUris) {
		this.redirectUris = redirectUris;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

}
