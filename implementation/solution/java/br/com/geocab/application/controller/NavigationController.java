package br.com.geocab.application.controller;

import java.util.Locale;
import java.util.Properties;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.geocab.application.ResourceBundleMessageSource;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.infrastructure.social.SpringSecuritySignInAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.geocab.domain.service.AccountService;

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