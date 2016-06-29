/**
 * 
 */
package br.com.geocab.infrastructure.social.mobile.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import br.com.geocab.domain.entity.configuration.account.User;
import br.com.geocab.infrastructure.social.mobile.SocialAuthentication;

/**
 * @author emanuelvictor
 *
 */
@Component
public class NormalAuthentication extends SocialAuthentication {
		
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#login(javax.servlet.http.HttpSession, java.lang.String)
	 */
	@Override
	public User login( User user, HttpServletRequest request )
	{
		user = this.validateUsername(user);
		
		Assert.isTrue(user.isEnabled(), "User is not enabled");
		
		user = this.validatePassword(user);
		
	    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
	    
	    // Authenticate the user
	    Authentication authentication =  authenticationManager.authenticate(authRequest);
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
	 * @see br.com.geocab.infrastructure.social.mobile.Authenticate#validateUsername()
	 */
	@Override
	public User validateUsername(User user)
	{
		// TODO Auto-generated method stub
		final User userReturn = loginService.findUserByEmail(user.getUsername());
		Assert.isTrue(userReturn != null, "Usuário não encontrado");
		userReturn.setPassword(user.getPassword());
		return userReturn;
	}
	
	/**
	 * 
	 */
	public User validatePassword(User user)
	{//TODO traduzir
		Assert.isTrue(loginService.findUserByEmail(user.getUsername()).getPassword().equals(passwordEncoder.encodePassword(user.getPassword(), SALT)), "Login ou password incorreto");
		return user;
	}

}
