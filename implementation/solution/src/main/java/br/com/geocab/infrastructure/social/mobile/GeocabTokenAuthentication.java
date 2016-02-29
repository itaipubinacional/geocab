/**
 * 
 */
package br.com.geocab.infrastructure.social.mobile;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * @author emanuelvictor
 *
 */
public class GeocabTokenAuthentication extends SocialAuthentication implements Authenticate
{

	/**
	 * @param accessToken
	 */
	public GeocabTokenAuthentication(String token, UserDetails user)
	{
		this.user = user;
		this.token = token;
	}
	
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#validateToken(java.lang.String)
	 */
	@Override
	public void validateToken()
	{//TODO traduzir token
		Assert.isTrue(generateToken(this.getUser().getUsername()).equals(token), "Invalid token");
	}

}
