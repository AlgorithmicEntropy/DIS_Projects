package de.dis2023.authentication;

/**
 * Small helper class for authentication
 */
public interface Authenticator {
	
	/**
	 * Reads in the authentication data and returns whether 
	 * the authentication was successful.
	 */
	public boolean authenticate();
}
