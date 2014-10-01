package br.com.geocab.infrastructure.social;

import org.springframework.social.connect.Connection;

/**
 * 
 * @author rodrigo
 */
public class ConnectionSignUp implements org.springframework.social.connect.ConnectionSignUp
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Sign up a new user of the application from the connection.
	 * @param connection the connection
	 * @return the new user id. May be null to indicate that an implicit local user profile could not be created.
	 */
	public String execute( Connection<?> connection )
	{
		//final UserProfile profile = connection.fetchUserProfile();
		return null;
	}

}