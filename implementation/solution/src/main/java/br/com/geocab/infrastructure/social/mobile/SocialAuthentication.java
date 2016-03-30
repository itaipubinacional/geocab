/**
 * 
 */
package br.com.geocab.infrastructure.social.mobile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.service.LoginService;
import br.com.geocab.infrastructure.social.mobile.provider.CustomAuthenticationProvider;

/**
 * @author emanuelvictor
 *
 */
public abstract class SocialAuthentication implements Authenticate
{
	/**
	 * Sabor para geração de token
	 */
	protected static final String SALT = "%$@#@!#g30cAA213#ck(398234";

	/**
	 * 
	 */
	@Autowired
	protected AuthenticationManager authenticationManager;
	/**
	 * 
	 */
	@Autowired
	protected ShaPasswordEncoder passwordEncoder;
	/**
	 * TODO devia ser a implementação do user service details ;'(
	 */
	@Autowired
	protected LoginService loginService;
	
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
	public User login( User user, HttpServletRequest request )
	{	
		user = validateToken(user);
		
		user = validateUsername(user);
		
		Assert.isTrue(user.isEnabled(), "User is not enabled");
		
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

	    CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider(user);
	    
	    // Authenticate the user
	    Authentication authentication =  customAuthenticationProvider.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    
	    securityContext.setAuthentication(authentication);
	    
	    // Create a new session and add the security context.
	    HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	    
	    user.setPassword(null);
	    user.setToken(SocialAuthentication.generateKey(user.getUsername()));
	    return user;
	}
	
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#validateToken(java.lang.String)
	 */
	@Override
	public User validateToken(User user)
	{//TODO traduzir
		Assert.isTrue(SocialAuthentication.generateKey(user.getUsername()).equals(user.getToken()), "Invalid token");
		return user;
	}

	/* (non-Javadoc)
	 * @see br.com.geocab.infrastructure.social.mobile.Authenticate#validateUsername()
	 */
	@Override
	public User validateUsername(User user)
	{		
		return loginService.loadOrSaveNewUserByUsername(user.getUsername());
	}
	
}
