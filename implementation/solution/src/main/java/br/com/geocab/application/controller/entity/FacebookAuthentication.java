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
public class FacebookAuthentication extends SocialAuthentication 
{
	
	/**
	 * @param accessToken
	 */
	public FacebookAuthentication(String token, UserDetails user)
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
		restTemplate.getForObject("https://graph.facebook.com/me?access_token="+token, String.class);
	}
	
}
