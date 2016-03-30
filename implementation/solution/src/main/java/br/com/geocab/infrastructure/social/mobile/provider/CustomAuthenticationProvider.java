/**
 * 
 */
package br.com.geocab.infrastructure.social.mobile.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author emanuelvictor
 *
 */
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