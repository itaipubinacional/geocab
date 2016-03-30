/**
 * 
 */
package br.com.geocab.infrastructure.social.mobile.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.infrastructure.social.mobile.SocialAuthentication;

/**
 * @author emanuelvictor
 *
 */
@Component
public class FacebookTokenAuthentication extends SocialAuthentication 
{
	/**
	 * 
	 */
	protected static final String URL = "https://graph.facebook.com/me?access_token=";
	
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#validateToken(java.lang.String)
	 */
	@Override
	public User validateToken(User user)
	{
		new RestTemplate().getForObject(URL+user.getToken(), String.class);
		return user;
	}
	
}
