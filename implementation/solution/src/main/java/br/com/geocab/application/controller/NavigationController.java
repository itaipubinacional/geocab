package br.com.geocab.application.controller;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import br.com.geocab.application.ResourceBundleMessageSource;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.repository.account.IUserRepository;

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
	
//	/**
//	 * 
//	 */
//	@Autowired
//	private AuthenticationManager authenticationManager;
	
	/**
	 * 
	 */
	@Autowired
	private IUserRepository userDetailsService;
	
	/**
	 * 
	 * @param request
	 * @param userName
	 * @param token
	 */
	@RequestMapping(value="/validateToken/{userName}/{token}", method = RequestMethod.GET)
	public @ResponseBody String validateToken(HttpServletRequest request, @PathVariable String userName, @PathVariable String token)
	{
		this.validateToken(token);
		
		UserDetails user = userDetailsService.loadUserByUsername(userName);
		
	    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

	    CustomAuthenticationProvider pro = new CustomAuthenticationProvider(user);
	    
	    // Authenticate the user
	    Authentication authentication =  pro.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    
	    securityContext.setAuthentication(authentication);
	    
	    // Create a new session and add the security context.
	    HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	    
	    return "Token valid";
	}
	
	/**
	 * 
	 * @param token
	 */
	private void validateToken(String token)
	{
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getForObject("https://graph.facebook.com/me?access_token="+token, String.class);
	}
	
	public class CustomAuthenticationProvider implements AuthenticationProvider {
		 
		/**
		 * 
		 */
		private UserDetails user;
	 
	    /**
		 * @param user
		 */
		public CustomAuthenticationProvider(UserDetails user)
		{
			super();
			this.user = user;
		}

		/**
		 * 
		 */
		@Override
	    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	    }
		
		/**
		 * 
		 */
	    @Override
	    public boolean supports(Class<?> arg0) {
	        return true;
	    }
	}
	
	
}