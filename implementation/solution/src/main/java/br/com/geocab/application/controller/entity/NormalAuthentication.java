/**
 * 
 */
package br.com.geocab.application.controller.entity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * @author emanuelvictor
 *
 */
public class NormalAuthentication extends SocialAuthentication implements Authenticate
{

	/**
	 * @param accessToken
	 */
	public NormalAuthentication(String token, UserDetails user)
	{
		this.user = user;
		this.token = token;
		this.restTemplate = new RestTemplate();
	}
	
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#validateToken(java.lang.String)
	 */
	@Override
	public void validateToken()
	{
		Assert.isTrue(generateToken(this.getUser().getUsername()) == token, "Invalid token");
	}

}
