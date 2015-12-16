package com.thomit.winecellar.exceptions;

public class UsernameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3699623122088128233L;

	public UsernameNotFoundException(String username){
		super("could not find user ' " + username + " '" );
	}
	

}
