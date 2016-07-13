package br.com.geocab.application.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.PlusOperations;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

<<<<<<< HEAD
import br.com.geocab.domain.entity.account.User;
=======
import br.com.geocab.domain.entity.configuration.account.User;
>>>>>>> 22ca1de34d48288e70521329e6a8095d94d71a26
import br.com.geocab.domain.service.LoginService;

/**
 * 
 * @author rodrigo
 *
 */
public class SocialAuthenticationFilter extends GenericFilterBean
{
	// ~ Instance fields
	// ================================================================================================
	
	private static final String AUTH_TOKEN = "access_token ";
	private static final String FACEBOOK = "facebook";
	private static final String GOOGLEPLUS = "googleplus";
	
	/**
	 * 
	 */
	@Autowired
	private LoginService loginService;	

	private AuthenticationEntryPoint authenticationEntryPoint;
	private AuthenticationManager authenticationManager;

	/**
	 * Creates an instance which will authenticate against the supplied
	 * {@code AuthenticationManager} and use the supplied
	 * {@code AuthenticationEntryPoint} to handle authentication failures.
	 *
	 * @param authenticationManager
	 *            the bean to submit authentication requests to
	 * @param authenticationEntryPoint
	 *            will be invoked when authentication fails. Typically an
	 *            instance of {@link BasicAuthenticationEntryPoint}.
	 */
	public SocialAuthenticationFilter( AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint )
	{
		this.authenticationManager = authenticationManager;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	// ~ Methods
	// ========================================================================================================

	/**
	 * 
	 */
	@Override
	public void afterPropertiesSet()
	{
		Assert.notNull(this.authenticationManager, "An AuthenticationManager is required");
		Assert.notNull(this.authenticationEntryPoint, "An AuthenticationEntryPoint is required");
	}

	/**
	 * 
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException
	{
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		String header = request.getHeader("Authorization");

		if ( header == null || !header.startsWith(AUTH_TOKEN))
		{
			chain.doFilter(request, response);
			return;
		}

		try
		{
			final String[] tokens = this.extractAndDecodeHeader(header, request);
			final String username = tokens[0];
							
			if ( authenticationIsRequired(username) ) {

				final String access_token = tokens[1];
				final String provider = tokens[2];
				String usernameToken = null;
				String profileName = null;
				
				if ( provider.equals(FACEBOOK) )
				{
					UserOperations userOperations = new FacebookTemplate(access_token).userOperations();
					usernameToken = userOperations.getUserProfile().getEmail();
					profileName = userOperations.getUserProfile().getName();
				} 
				else if ( provider.equals(GOOGLEPLUS) )
				{
					PlusOperations plusOperations = new GoogleTemplate(access_token).plusOperations();
					usernameToken = plusOperations.getGoogleProfile().getAccountEmail();
					profileName = plusOperations.getGoogleProfile().getDisplayName();
				}
				
				if ( usernameToken == null || !usernameToken.equals(username) )
				{
					throw new BadCredentialsException("Invalid access authentication token");
				}
				
				User user = this.loginService.findUserByEmail(username);
				
				if( user != null && user.isEnabled() == false )
				{
					throw new BadCredentialsException("Invalid credentials - User disabled");
				} 
				else if( user == null )
				{
					user = this.loginService.insertSocialUser(new User(profileName, username));
				}
				
				final SecurityContext securityContext = SecurityContextHolder.getContext();
				securityContext.setAuthentication( new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()) );
				
			}
		}
		catch ( AuthenticationException failed )
		{
			SecurityContextHolder.clearContext();
			authenticationEntryPoint.commence(request, response, failed);
			return;
		}

		chain.doFilter(request, response);
	}

	/**
	 * Decodes the header into a username and password.
	 *
	 * @throws BadCredentialsException
	 *             if the Basic header is not present or is not valid Base64
	 */
	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException
	{
		final byte[] base64Token = header.substring(AUTH_TOKEN.length()).getBytes("UTF-8");
		byte[] decoded;
		try
		{
			decoded = Base64.decode(base64Token);
		}
		catch (IllegalArgumentException e)
		{
			throw new BadCredentialsException("Failed to decode the access token");
		}

		final String token = new String(decoded, "UTF-8");

		final int delimUser = token.indexOf(":");
		final int delimProvider = token.indexOf(":", delimUser + 1);
		
		if ( delimUser == -1 || delimProvider == -1 )
		{
			throw new BadCredentialsException("Invalid access authentication token");
		}
		
		return new String[]{ 
			token.substring(0, delimUser), 
			token.substring(delimUser + 1, delimProvider),
			token.substring(delimProvider + 1)
		};
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	private boolean authenticationIsRequired(String username)
	{
		// Only reauthenticate if username doesn't match SecurityContextHolder
		// and user isn't authenticated
		// (see SEC-53)
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

		if (existingAuth == null || !existingAuth.isAuthenticated())
		{
			return true;
		}

		// Limit username comparison to providers which use usernames (ie
		// UsernamePasswordAuthenticationToken)
		// (see SEC-348)

		if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username))
		{
			return true;
		}

		// Handle unusual condition where an AnonymousAuthenticationToken is
		// already present
		// This shouldn't happen very often, as BasicProcessingFitler is meant
		// to be earlier in the filter
		// chain than AnonymousAuthenticationFilter. Nevertheless, presence of
		// both an AnonymousAuthenticationToken
		// together with a BASIC authentication request header should indicate
		// reauthentication using the
		// BASIC protocol is desirable. This behaviour is also consistent with
		// that provided by form and digest,
		// both of which force re-authentication if the respective header is
		// detected (and in doing so replace
		// any existing AnonymousAuthenticationToken). See SEC-610.
		if (existingAuth instanceof AnonymousAuthenticationToken)
		{
			return true;
		}

		return false;
	}
}
