package br.com.geocab.application.controller;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.geocab.application.ResourceBundleMessageSource;
import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.configuration.account.User;
import br.com.geocab.domain.entity.configuration.account.UserRole;

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
	public ModelAndView admin(HttpServletRequest request)
	{
		return new ModelAndView("modules/admin/ui/index"  );
	}
	
	/**
	 * 
	 */
	@RequestMapping( value="/admin/markers", method=RequestMethod.GET )
	public ModelAndView markers(HttpServletRequest request)
	{
		if (request.getParameter("id")!= null)
		{
			return new ModelAndView("redirect:/admin#/markers?id=" + request.getParameter("id"));
		}
		return new ModelAndView("redirect:/admin"  );
	}
	
	/**
	 * 
	 */
	@RequestMapping( value="/authentication", method=RequestMethod.GET )
	public String authentication( Locale locale,HttpServletRequest request) 
	{
		User user = ContextHolder.getAuthenticatedUser();
		if (user == null || user.getRole().equals(UserRole.ANONYMOUS))
		{
			return "modules/authentication/ui/index";
		}
		return "redirect:/";
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