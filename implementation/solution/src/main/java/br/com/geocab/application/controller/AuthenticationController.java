package br.com.geocab.application.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.geocab.application.controller.entity.FacebookTokenAuthentication;
import br.com.geocab.application.controller.entity.GeocabTokenAuthentication;
import br.com.geocab.application.controller.entity.GooglePlusTokenAuthentication;
import br.com.geocab.application.controller.entity.NormalAuthentication;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.repository.account.IUserRepository;
import br.com.geocab.domain.service.LoginService;


/**
 * 
 * @author Emanuel Victor
 * @since 23/02/2016
 * @version 1.0
 * @category
*/
@Controller 
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
	/**
	 * 
	 */
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	/*-------------------------------------------------------------------
	 * 		 				 		BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param accessToken
	 */
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public @ResponseBody StringBuffer login(HttpServletRequest request, @RequestBody User user)
	{	
		verifyUser(user.getUsername());
		return new StringBuffer(new NormalAuthentication(user, userDetailsService, authenticationManager, passwordEncoder).login(request));
	}
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/login/facebook", method = RequestMethod.GET)
	public @ResponseBody StringBuffer facebookLogin(HttpServletRequest request, @RequestParam String userName, @RequestParam String token)
	{
		verifyUser(userName);
		return new StringBuffer(new FacebookTokenAuthentication(token, userDetailsService.loadUserByUsername(userName)).login(request));
	}
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/login/google", method = RequestMethod.GET)
	public @ResponseBody StringBuffer googleLogin(HttpServletRequest request, @RequestParam String userName, @RequestParam String token)
	{	
		verifyUser(userName);
		return new StringBuffer(new GooglePlusTokenAuthentication(token, userDetailsService.loadUserByUsername(userName)).login(request));
	}
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param token
	 */
	@RequestMapping(value="/login/geocab", method = RequestMethod.GET)
	public @ResponseBody StringBuffer normalLogin(HttpServletRequest request, @RequestParam String userName, @RequestParam String token)
	{
		verifyUser(userName);
		return new StringBuffer(new GeocabTokenAuthentication(token, userDetailsService.loadUserByUsername(userName)).login(request));
	}
	@Autowired
	LoginService loginService;
	/**
	 * TODO colocar pra dentro das entidades
	 * @param userName
	 */
	private void verifyUser(String userName)
	{
		try
		{
			userDetailsService.loadUserByUsername(userName);
		}
		catch (UsernameNotFoundException e)
		{
			User user = new User(userName, userName);
			user.getBackgroundMap();
			user.getCoordinates();
			loginService.insertSocialUser(user);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	

}