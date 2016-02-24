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
public class GooglePlusAuthentication  extends SocialAuthentication implements Authenticate
{

	/**
	 * @param accessToken
	 */
	public GooglePlusAuthentication(String token, UserDetails user)
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
		restTemplate.getForObject("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token="+token, String.class);
	}
	
}
