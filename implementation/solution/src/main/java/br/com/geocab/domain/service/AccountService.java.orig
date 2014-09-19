package br.com.geocab.domain.service;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	 * Gerador Hash para criptografia
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
	public User insertUser( User user )//TODO: only admin
	{
		Assert.notNull( user );
		
		user.setEnabled(true);
		
		//encrypt password
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); 
		user.setPassword( encodedPassword );
		
		return this.userRepository.save( user );
	}
	
	public User createUser( User user )
	{
		Assert.notNull( user );
		
		user.setEnabled(true);
		user.setRole(UserRole.USER);
				
		//encrypt password
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); 
		user.setPassword( encodedPassword );
		
		return this.userRepository.save( user );
	}
	
	/**
	 * Método para listar as fontes de dados paginadas com opção do filtro
	 *
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<User> listUsersByFilters( String filter, PageRequest pageable )
	{
		return this.userRepository.listByFilters(filter, pageable);
	}
}
