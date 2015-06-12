package br.com.geocab.application.controller;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.geocab.application.ResourceBundleMessageSource;
import br.com.geocab.domain.service.AccountService;
import br.com.geocab.infrastructure.social.SpringSecuritySignInAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Rodrigo P. Fraga
 * @since 13/05/2013
 * @version 1.0
 * @category
 */
@Controller
public class NavigationController
{
	/*-------------------------------------------------------------------
	 * 		 				 		ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private ResourceBundleMessageSource messageSource;
	/**
	 * 
	 */
	@Autowired
	private SpringSecuritySignInAdapter signInAdapter;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 
	 */
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	/*-------------------------------------------------------------------
	 * 		 				 		BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@RequestMapping( value="/", method=RequestMethod.GET )
	public ModelAndView index()
	{
		return new ModelAndView("modules/map/ui/index");
	}
	
	/**
	 * 
	 */
	@RequestMapping( value="/user", method=RequestMethod.GET )
	public ModelAndView user()
	{
		return new ModelAndView("modules/user/ui/index");
	}
	
	/**
	 * 
	 */
	@RequestMapping( value="/admin", method=RequestMethod.GET )
	public ModelAndView admin()
	{
		return new ModelAndView("modules/admin/ui/index");
	}
	
	/**
	 * 
	 */
	@RequestMapping( value="/authentication", method=RequestMethod.GET )
	public String authentication( Locale locale )
	{
		return "modules/authentication/ui/index";
	}
	
	/**
	 * 
	 */
	@RequestMapping( value="/signin", method=RequestMethod.GET )
	public String signinError( @RequestParam String error, HttpServletRequest httpServletRequest )
	{
		System.out.println( error );
		return "modules/signin";
	}
	
	/**
	 * 
	 */
	@RequestMapping( value="/signup", method=RequestMethod.GET )
	public String signupError( @ModelAttribute("error") String error, HttpServletRequest httpServletRequest )
	{
		System.out.println( error );
		return null;
	}


	/**
	 * 
	 * @param lang
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/bundles", method = RequestMethod.GET)
    public Properties listMessageBundles( Locale locale ) 
	{
        return this.messageSource.getProperties( locale );
    }
}