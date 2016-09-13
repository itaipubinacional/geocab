package br.com.geocab.infrastructure.social;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;

import br.com.geocab.domain.entity.configuration.account.User;
import br.com.geocab.domain.repository.account.IUserRepository;
import br.com.geocab.infrastructure.social.mobile.SocialAuthentication;
import br.com.geocab.infrastructure.social.mobile.provider.CustomAuthenticationProvider;

/**
 * 
 * @author rodrigo
 */
public class SpringSecuritySignInAdapter implements SignInAdapter
{
	/**
	 * 
	 */
	@Autowired
	private IUserRepository userRepository;
	
	/**
	 * Complete a provider sign-in attempt by signing in the local user account with the specified id.
	 * @param userId the local user id
	 * @param connection the connection
	 * @param request a reference to the current web request; is a "native" web request instance providing access to the native
	 * request and response objects, such as a HttpServletRequest and HttpServletResponse, if needed
	 * @return the URL that ProviderSignInController should redirect to after sign in. May be null, indicating that ProviderSignInController
	 * should redirect to its postSignInUrl.
	 */
	public String signIn( String userId, Connection<?> connection, NativeWebRequest nativeWebRequest )
	{
//		final User user = this.userRepository.findByEmail(userId);
//		final HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
//		final HttpSession session = request.getSession(true);
//		
//		if( false == user.isEnabled() ){
//			final DisabledException erro = new DisabledException("User is disabled" );
//			
//			session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION" , erro );
//			return null;
//		}
//		
//		
//		final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
//		
//	    final CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider(user);
//		
//	 // Authenticate the user
//	    final Authentication authentication =  customAuthenticationProvider.authenticate(authRequest);
//	  final  SecurityContext securityContext = SecurityContextHolder.getContext();
//	    
//	    securityContext.setAuthentication(authentication);
//		
//		
////		final SecurityContext securityContext = SecurityContextHolder.getContext();
////		securityContext.setAuthentication( new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()) );
//			    
//		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
//		
//		return null;//redirects to /
		
		final HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
		
		final User user = this.userRepository.findByEmail(userId);
		
		Assert.isTrue(user.isEnabled(), "User is not enabled");
		
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

	    CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider(user);
	    
	    // Authenticate the user
	    Authentication authentication =  customAuthenticationProvider.authenticate(authRequest);
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    
	    securityContext.setAuthentication(authentication);
	    
	    // Create a new session and add the security context.
	    HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	    
	    user.setPassword(null);
	    user.setToken(SocialAuthentication.generateKey(user.getUsername()));
	    return null;
	}
}