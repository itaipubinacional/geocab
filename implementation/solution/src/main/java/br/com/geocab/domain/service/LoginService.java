/**
 * 
 */
package br.com.geocab.domain.service;


import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.repository.account.IUserRepository;

/**
 * @author Thiago Rossetto Afonso
 * @since 29/09/2014
 * @version 1.0
 */

@Service
@Transactional
@RemoteProxy(name="loginService")
public class LoginService
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * Password encoder
	 */
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	
	/**
	 * Hash generator for encryption
	 */
	@Autowired
	private SaltSource saltSource;
	
	/**
	 * User Repository
	 */
	@Autowired
	private IUserRepository userRepository;
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * Insert a new User
	 * 
	 * @param user
	 * @return
	 */
	public User insertUser( User user )
	{
		Assert.notNull( user );
		
		user.setRole(UserRole.USER);
		user.setEnabled(true);
		
		//encrypt password
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); 
		user.setPassword( encodedPassword );
		
		return this.userRepository.save( user );
	}
	
	@Transactional(readOnly = true)
	public User findUserByEmail( String userName )
	{
		return this.userRepository.findByEmail( userName );
	}
	
	public User authenticatedUser()
	{
		User user = this.userRepository.save(ContextHolder.getAuthenticatedUser());		
		return user;
	}
	
	/**
	 * Realiza o logout do sistema
	 * @param user
	 */
	public void logout()
	{
		SecurityContextHolder.clearContext();
		SecurityContextHolder.createEmptyContext();
	}
	
	
}
