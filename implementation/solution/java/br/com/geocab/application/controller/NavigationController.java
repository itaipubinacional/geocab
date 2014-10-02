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
import br.com.geocab.infrastructure.social.SpringSecuritySignInAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;

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
	 * @param request
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public ModelAndView signup( RedirectAttributes redirectAttributes, WebRequest request ) throws JsonProcessingException 
	{
		//Maybe the URL was not called from a provider... 
	    final Connection<?> connection = new ProviderSignInUtils().getConnectionFromSession(request);
	    if ( connection == null )
	    {
	    	return new ModelAndView("redirect:/authentication");
	    }
	    
	    final UserProfile profile = connection.fetchUserProfile();
		if ( profile.getEmail() == null ) return null;
		String name = profile.getName();
		if ( name == null )
		{
			if ( profile.getFirstName() != null && profile.getLastName() != null )
			{
				name = profile.getFirstName() + " " + profile.getLastName();
			}
			if ( name == null )
			{
				name = profile.getEmail();
			}
		}
		final User user = new User();
		user.setEmail(profile.getEmail());
		user.setName(name);
		
		//Write the user generated from provider to the page as json
//		final String userJson = objectMapper.writeValueAsString( user );
//		redirectAttributes.addFlashAttribute("user", userJson);
		
		//redirect to the authentication with sigup state
	    return new ModelAndView("redirect:/authentication/#/signup");
	}
	
	/**
	 * 
	 * @param form
	 * @param formBinding
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public void signup( @Valid User user, NativeWebRequest request ) 
	{
//	    user = this.accountService.createAccount(user);
//	    new ProviderSignInUtils().doPostSignUp(user.getUsername(), request);
	    
//	    final Connection<?> connection = new ProviderSignInUtils().getConnectionFromSession(request);
//	    this.signInAdapter.signIn( user.getUsername() , connection, request);
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