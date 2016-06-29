package br.com.geocab.application.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.geocab.domain.entity.configuration.account.User;
import br.com.geocab.infrastructure.social.mobile.impl.FacebookTokenAuthentication;
import br.com.geocab.infrastructure.social.mobile.impl.GeocabTokenAuthentication;
import br.com.geocab.infrastructure.social.mobile.impl.GooglePlusTokenAuthentication;
import br.com.geocab.infrastructure.social.mobile.impl.NormalAuthentication;

/**
 * 
 * @author Emanuel Victor
 * @since 23/02/2016
 * @version 1.0
 * @category
*/
@Controller
public class MobileAuthenticationController
{
	/*-------------------------------------------------------------------
	 * 		 				 		ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	@Autowired
	private NormalAuthentication normalAuthentication;
	
	/**
	 * 
	 */
	@Autowired
	private GooglePlusTokenAuthentication googlePlusTokenAuthentication;
	
	/**
	 * 
	 */
	@Autowired
	private FacebookTokenAuthentication facebookTokenAuthentication;
	
	/**
	 * 
	 */
	@Autowired
	private GeocabTokenAuthentication geocabTokenAuthentication;
	
	/*-------------------------------------------------------------------
	 * 		 				 		BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public @ResponseBody User login(HttpServletRequest request, @RequestBody User user)
	{	
		return normalAuthentication.login(user, request);
	}
	
	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/login/facebook", method = RequestMethod.POST)
	public @ResponseBody User facebookLogin(HttpServletRequest request, @RequestBody User user)
	{
		return facebookTokenAuthentication.login(user, request);
	}
	
	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/login/google", method = RequestMethod.POST)
	public @ResponseBody User googleLogin(HttpServletRequest request, @RequestBody User user)
	{	
		return googlePlusTokenAuthentication.login(user, request);
	}
	
	/**
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/login/geocab", method = RequestMethod.POST)
	public @ResponseBody User geocabLogin(HttpServletRequest request, @RequestBody User user)
	{
		return geocabTokenAuthentication.login(user, request);
	}
}