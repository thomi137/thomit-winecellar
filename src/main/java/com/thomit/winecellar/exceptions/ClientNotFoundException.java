package com.thomit.winecellar.exceptions;

public class ClientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3699623122088128233L;

	public ClientNotFoundException(String clientId){
		super("could not find client ' " + clientId + " '" );
	}
	

}
