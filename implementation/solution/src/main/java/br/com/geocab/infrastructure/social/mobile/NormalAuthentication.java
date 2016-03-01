/**
 * 
 */
package br.com.geocab.infrastructure.social.mobile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

/**
 * @author emanuelvictor
 *
 */
public class NormalAuthentication extends SocialAuthentication implements Authenticate
{
	/**
	 * 
	 * @param user
	 * @param userDetails
	 * @param authenticationManager
	 * @param passwordEncoder
	 */
	public NormalAuthentication( UserDetails user, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, ShaPasswordEncoder passwordEncoder)
	{
		this.user = user;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}
	
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#login(javax.servlet.http.HttpSession, java.lang.String)
	 */
	@Override
	public String login(HttpServletRequest request)
	{
		
		this.validateToken();
		
	    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), userDetailsService.loadUserByUsername(user.getUsername()).getAuthorities());
	    
	    // Authenticate the user
	    Authentication authentication =  authenticationManager.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    
	    securityContext.setAuthentication(authentication);
	    
	    // Create a new session and add the security context.
	    HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	    
		return SocialAuthentication.generateKey(user.getUsername());
	}
	
	/* (non-Javadoc)
	 * @see br.com.geocab.application.controller.entity.Authenticate#validateToken(java.lang.String)
	 */
	@Override
	public void validateToken()
	{//TODO traduzir
		Assert.isTrue(userDetailsService.loadUserByUsername(user.getUsername()).getPassword().equals(passwordEncoder.encodePassword(user.getPassword(), "%$@#@!#g30cAA213#ck(398234")), "Login ou password incorreto");
	}

}
