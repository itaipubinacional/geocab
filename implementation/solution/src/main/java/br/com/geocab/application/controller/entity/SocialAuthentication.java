/**
 * 
 */
package br.com.geocab.application.controller.entity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * @author emanuelvictor
 *
 */
public abstract class SocialAuthentication implements Authenticate
{
	/**
	 * Salto para geração de token
	 */
	private static final String SALT = "320bbf6f702da59b1af3ee7b3a5359f1";
	/**
	 * 
	 */
	protected UserDetails user;
	/**
	 * 
	 */
	protected String token;
	/**
	 * 
	 */
	protected RestTemplate restTemplate;

	/**
	 * 
	 */
	public SocialAuthentication()
	{
		super();
		this.restTemplate = new RestTemplate();
	}

	/**
	 * @return the restTemplate
	 */
	public RestTemplate getRestTemplate()
	{
		return restTemplate;
	}

	/**
	 * @param restTemplate
	 *            the restTemplate to set
	 */
	public void setRestTemplate(RestTemplate restTemplate)
	{
		this.restTemplate = restTemplate;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token)
	{
		this.token = token;
	}

	/**
	 * @return the user
	 */
	public UserDetails getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserDetails user)
	{
		this.user = user;
	}
	
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#generateToken(java.lang.String)
	 */
	@Override
	public String generateToken(String userName)
	{
		return generateKey(userName);
	}
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public static String generateKey( String username ){
		return DigestUtils.md5Hex(username + SALT);
	}
	
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#login(javax.servlet.http.HttpSession, java.lang.String)
	 */
	@Override
	public String login(HttpServletRequest request)
	{
		Assert.isTrue(user.isEnabled(), "User is not enabled");
		
		validateToken();
		
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

	    CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider(user);
	    
	    // Authenticate the user
	    Authentication authentication =  customAuthenticationProvider.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    
	    securityContext.setAuthentication(authentication);
	    
	    // Create a new session and add the security context.
	    HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	    
	    return this.generateToken(user.getUsername());
	}
}
