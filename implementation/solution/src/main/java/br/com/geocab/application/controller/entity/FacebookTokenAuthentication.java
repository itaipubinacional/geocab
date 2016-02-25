/**
 * 
 */
package br.com.geocab.application.controller.entity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

/**
 * @author emanuelvictor
 *
 */
public class FacebookTokenAuthentication extends SocialAuthentication 
{
	
	/**
	 * @param accessToken
	 */
	public FacebookTokenAuthentication(String token, UserDetails user)
	{
		this.user = user;
		this.token = token;
	}

	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#validateToken(java.lang.String)
	 */
	@Override
	public void validateToken()
	{
		new RestTemplate().getForObject("https://graph.facebook.com/me?access_token="+token, String.class);
	}
	
}
