package br.com.geocab.infrastructure.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.service.LoginService;

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
	private LoginService loginService;
	
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
		
		User user = this.loginService.findUserByEmail( profile.getEmail() );
		
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
			user.setRole(UserRole.USER);
			user.setEnabled(true);
			user = this.loginService.insertSocialUser(user);
			
		}else if( !user.isEnabled() ){//Disabled User
			return null; 
		}
		
		return user.getUsername();
	}

}