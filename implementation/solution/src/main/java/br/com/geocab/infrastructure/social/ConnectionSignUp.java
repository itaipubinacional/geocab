package br.com.geocab.infrastructure.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.service.AccountService;

/**
 * 
 * @author rodrigo
 */
public class ConnectionSignUp implements org.springframework.social.connect.ConnectionSignUp
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	@Autowired
	private AccountService accountService;
	
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
		final UserProfile profile = connection.fetchUserProfile();
		
		if ( profile.getEmail() == null ) return null;
		
		User user = this.accountService.findUserByEmail( profile.getEmail() );
		
		if( null == user ){ //Create if not exist 
			
			String name = profile.getName();
			
			if ( name == null )
			{
				if ( profile.getFirstName() != null && profile.getLastName() != null )
				{
					name = profile.getFirstName() + " " + profile.getLastName();
				}
				if ( name == null )
				{
					name = profile.getEmail();
				}
			}
			
			user = new User();
			user.setEmail(profile.getEmail());
			user.setName(name);
			user.setPassword("123"); //TODO: Random password
			user.setRole(UserRole.USER);
			user = this.accountService.insertUser( user );
		}
		
		return user.getUsername();
	}

}