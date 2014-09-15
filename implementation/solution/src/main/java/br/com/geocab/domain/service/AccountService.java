package br.com.geocab.domain.service;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.repository.account.IUserRepository;

/**
 * 
 * @author Rodrigo P. Fraga 
 * @since 22/04/2014
 * @version 1.0
 * @category Service
 */
@Service
@Transactional
@RemoteProxy(name="accountService")
public class AccountService
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Encripitador de senha 
	 */
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	/**
	 * Gerardor Hash para criptografia
	 */
	@Autowired
	private SaltSource saltSource;
	
	//Repositories
	/**
	 * 
	 */
	@Autowired
	private IUserRepository userRepository;
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * @param user
	 * @return
	 */
	public User insertUser( User user )
	{
		Assert.notNull( user );
		
		user.setEnabled(true);
		user.setRole(UserRole.USER);
		
		//Criptografa a senha
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); 
		user.setPassword( encodedPassword );
		
		return this.userRepository.save( user );
	}
}
