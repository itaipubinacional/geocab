package br.com.geocab.application.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.geocab.application.controller.entity.FacebookAuthentication;
import br.com.geocab.application.controller.entity.GooglePlusAuthentication;
import br.com.geocab.application.controller.entity.NormalAuthentication;
import br.com.geocab.application.controller.entity.SocialAuthentication;
import br.com.geocab.domain.repository.account.IUserRepository;


/**
 * 
 * @author Rodrigo P. Fraga
 * @since 13/05/2013
 * @version 1.0
 * @category
@Controller
 */
public class AuthenticationController
{
	/*-------------------------------------------------------------------
	 * 		 				 		ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private IUserRepository userDetailsService;
	
	/**
	 * 
	 */
	@Autowired
	private AuthenticationManager authenticationManager;
	/*-------------------------------------------------------------------
	 * 		 				 		BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param accessToken
	 */
	@RequestMapping(value="/login/{userName}/{password}", method = RequestMethod.GET) //TODO alterar para post
	public @ResponseBody String login(HttpServletRequest request, @PathVariable String userName, @PathVariable String password)
	{	
		UserDetails user = userDetailsService.loadUserByUsername(userName);
		// TODO colocar PADRÃO DE PROJETO AQUI TAMBÉM
	    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password, user.getAuthorities());
	    
	    // Authenticate the user
	    Authentication authentication =  authenticationManager.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    
	    securityContext.setAuthentication(authentication);
	    
	    // Create a new session and add the security context.
	    HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	    
	    return SocialAuthentication.generateKey(userName);
	}
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param token
	 */
	@RequestMapping(value="/login/facebook/{userName}/{token}", method = RequestMethod.GET)
	public @ResponseBody StringBuffer loginFacebook(HttpServletRequest request, @PathVariable String userName, @PathVariable String token)
	{
		return new StringBuffer(new FacebookAuthentication(token, userDetailsService.loadUserByUsername(userName)).login(request));
	}
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param token
	 */
	@RequestMapping(value="/login/googleplus/{userName}/{token}", method = RequestMethod.GET)
	public @ResponseBody StringBuffer loginGooglePlus(HttpServletRequest request, @PathVariable String userName, @PathVariable String token)
	{
		return new StringBuffer(new GooglePlusAuthentication(token, userDetailsService.loadUserByUsername(userName)).login(request));
	}
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/login/normal/{userName}/{token}", method = RequestMethod.GET)
	public @ResponseBody StringBuffer validateToken(HttpServletRequest request, @PathVariable String userName, @PathVariable String token)
	{
		return new StringBuffer(new NormalAuthentication(token, userDetailsService.loadUserByUsername(userName)).login(request));
	}
}