/**
 * 
 */
package br.com.geocab.domain.service;

import java.util.logging.Logger;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
//@PreAuthorize("hasRole('"+UserRole.ADMINISTRADOR_VALUE+"')")
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
	
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger( AccountService.class.getName() );

	
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
	
	/**
	 * Update User
	 * 
	 * @param User
	 * @return User
	 */
	public User updateUser( User user )
	{			
		try{
			User dbUser = this.userRepository.findOne(user.getId());
			
			//Update database user
			dbUser.setEmail(user.getEmail());
			dbUser.setName(user.getName());
			dbUser.setRole(user.getRole());
			dbUser.setEnabled(true);
			
			if( !user.getPassword().isEmpty() ){ //if set new password
				final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( dbUser ) ); 
				dbUser.setPassword( encodedPassword );
			}
						
			user = this.userRepository.save( dbUser );//save data in database
			
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			final String error = e.getCause().getCause().getMessage();
		}
		
		return user;
	}

}
