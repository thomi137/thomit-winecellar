package com.thomit.winecellar.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String clientId;
	String clientSecret;
	String resourceIds;
	String scopes;
	String grantTypes;
	String authorities;

	public Client(String clientId, String clientSecret, String resourceIds,
			String scopes, String grantTypes, String authorities) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.resourceIds = resourceIds;
		this.scopes = scopes;
		this.grantTypes = grantTypes;
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
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

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

}
