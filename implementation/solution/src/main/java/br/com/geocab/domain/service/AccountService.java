package br.com.geocab.domain.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.accessgroup.AccessGroup;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.layer.LayerGroup;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupRepository;
import br.com.geocab.domain.repository.account.IUserRepository;

/**
 * 
 * @author Cristiano Correa 
 * @since 22/04/2014
 * @version 1.0
 * @category Service
 */
@Service
@Transactional
@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
@RemoteProxy(name="accountService")
public class AccountService
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * User Repository
	 */
	@Autowired
	private IUserRepository userRepository;
	
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger( AccountService.class.getName() );
	
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
	 * 
	 */
	@Autowired
	private IAccessGroupRepository accessGroupRepository;

	
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
		
		user.setEnabled(true);
		//encrypt password
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); 
		user.setPassword( encodedPassword );
		
		user = this.userRepository.save( user );
		
		AccessGroup publicAccessGroup = this.accessGroupRepository.findOne(AccessGroup.PUBLIC_GROUP_ID);
		
		publicAccessGroup.getUsers().add(user);
		
		this.accessGroupRepository.save(publicAccessGroup);
		
		return user;
	}
		
	/**
	 * List Users with pagination and filters
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
	
	@Transactional(readOnly=true)
	public List<User> listAllUsers()
	{
		return this.userRepository.findAll();
	}
	
	/**
	 * Find User by id
	 * 
	 * @param id
	 * @return User
	 */
	@Transactional(readOnly = true)
	public User findUserById( Long id )
	{
		return this.userRepository.findOne( id );
	}
	
	/**
	 * Find User by userName
	 * 
	 * @param userName
	 * @return User
	 */
	@Transactional(readOnly = true)
	public User findUserByEmail( String userName )
	{
		return this.userRepository.findByEmail( userName );
	}
	
	/**
	 * Disable User
	 * 
	 * @param id
	 * @return boolean 
	 */
	public Boolean disableUser( Long id )
	{
		User user = this.userRepository.findOne( id ); //Load user
		user.setEnabled(false); //Disable user
		this.userRepository.save( user ); //Save user
		
		return true;
	}
	
	/**
	 * Enable User
	 * 
	 * @param id
	 * @return boolean
	 */
	public Boolean enableUser( Long id )
	{	
		User user = this.userRepository.findOne( id ); //Load user
		user.setEnabled(true); //Enable user
		this.userRepository.save( user ); //Save user
		
		return true;
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
	
	/**
	 * Update User
	 * 
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@PreAuthorize("permitAll")
	public User getUserAuthenticated() throws Exception
	{
		User user = this.userRepository.findOne(ContextHolder.getAuthenticatedUser().getId());
		
		User u =new User();
		u.setCreated(user.getCreated());
		u.setEmail(user.getEmail());
		u.setEnabled(user.getEnabled());
		u.setId(user.getId());
		u.setName(user.getName());
		u.setRole(user.getRole());
		u.setUpdated(user.getUpdated());
		
		return u;
	}
	
	/**
	 * Update User
	 * 
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@PreAuthorize("permitAll")
	public User updateUserAuthenticated( User u ) throws Exception
	{
		Assert.notNull( u );
		
		User userAuthencated = ContextHolder.getAuthenticatedUser();
		
		User user = this.findUserByEmail(userAuthencated.getEmail());
		
		if(user == null){
			throw new Exception("Usuário inexistente!");
		}
		
		if(!this.passwordEncoder.encodePassword( u.getPassword(), saltSource.getSalt( u ) ).equals(user.getPassword())) {
			throw new Exception("A senha informada não é correspondente!");
		}
		
		user.setName(u.getName());
		user.setRole(userAuthencated.getRole());
		
		if(u.getNewPassword() != null) {	
			final String encodedPassword = this.passwordEncoder.encodePassword( u.getNewPassword(), saltSource.getSalt( u ) ); 
			user.setPassword( encodedPassword );
		}
		
		return this.userRepository.save( user );
	}
	
}
