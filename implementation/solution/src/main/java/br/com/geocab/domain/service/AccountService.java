package br.com.geocab.domain.service;

import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.geocab.application.ResourceBundleMessageSource;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.datasource.DataSource;
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
//@PreAuthorize("hasRole('"+UserRole.ADMINISTRADOR_VALUE+"')")
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
	
	
	private static final Logger LOG = Logger.getLogger( AccountService.class.getName() );

	
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
		
		//encrypt password
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); 
		user.setPassword( encodedPassword );
		
		return this.userRepository.save( user );
	}
	
//	public User createUser( User user )
//	{
//		Assert.notNull( user );
//		
//		user.setEnabled(true);
//		user.setRole(UserRole.USER);
//				
//		//encrypt password
//		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); 
//		user.setPassword( encodedPassword );
//		
//		return this.userRepository.save( user );
//	}
	
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
	
	/**
	 * 
	 * @param id
	 * @return User
	 * @throws JAXBException 
	 */
	@Transactional(readOnly = true)
	public User findUserById( Long id )
	{
		return this.userRepository.findOne( id );
	}
	
	/**
	 * 
	 * @param id
	 * @return boolean
	 * @throws JAXBException 
	 */
	public Boolean disableUser( Long id )
	{
		User user = this.userRepository.findOne( id ); //Load user
		user.setEnabled(false); //Disable user
		this.userRepository.save( user ); //Save user
		
		return true;
	}
	
	/**
	 * 
	 * @param id
	 * @return boolean
	 * @throws JAXBException 
	 */
	public Boolean enableUser( Long id )
	{	
		User user = this.userRepository.findOne( id ); //Load user
		user.setEnabled(true); //Enable user
		this.userRepository.save( user ); //Save user
		
		return true;
	}
	
	/**
	 * 
	 * @param User
	 * @return User
	 */
	public User updateUser( User user )
	{			
		try{
			User dbUser = this.userRepository.findOne(user.getId());
			
			dbUser.setEmail(user.getEmail());
			dbUser.setName(user.getName());
			dbUser.setRole(user.getRole());
			
			if( !user.getPassword().isEmpty() ){ //if set new password
				final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( dbUser ) ); 
				dbUser.setPassword( encodedPassword );
			}
						
			user = this.userRepository.save( dbUser );
			
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			final String error = e.getCause().getCause().getMessage();
			//TODO: Tratar error.
		}
		
		return user;
	}
}
