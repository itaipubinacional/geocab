package br.com.geocab.application.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.geocab.domain.entity.account.User;

/**
 *
 * @author rodrigo
 * @since 24/07/2013
 * @version 1.0
 * @category
 */
public class ContextHolder
{
	/**
	 *
	 * @return
	 */
	public static User getAuthenticatedUser()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if ( authentication != null )
		{
			if ( authentication.getPrincipal() instanceof User )
			{
				return (User) authentication.getPrincipal();
			}
			
			if ( authentication instanceof AnonymousAuthenticationToken )
			{
				return User.ANONYMOUS;
			}
		}
		
		throw new InsufficientAuthenticationException("There is no user authenticated or even anonymous.");//FIXME Localize
	}
}
