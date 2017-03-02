/**
 * 
 */
package br.com.geocab.domain.service;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.accessgroup.AccessGroup;
import br.com.geocab.domain.entity.account.IAccountMailRepository;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupRepository;
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
	
	/**
	 * AccountMail Repository
	 */
	@Autowired
	private IAccountMailRepository accountMailRepository;
	
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
		
		user.setRole(UserRole.USER);
		user.setEnabled(true);
	
		//encrypt password
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); 
		user.setPassword( encodedPassword );
		
		User u = this.userRepository.save( user );
		
		AccessGroup publicAccessGroup = this.accessGroupRepository.findOne(1L);
		
		publicAccessGroup.getUsers().add(u);
		
		this.accessGroupRepository.save(publicAccessGroup);
		
		return u;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public User insertSocialUser( User user )
	{
		Assert.notNull( user );
		
		user.setRole(UserRole.USER);
		user.setEnabled(true);
		
		/*//encrypt password
		final String encodedPassword = this.passwordEncoder.encodePassword( user.getPassword(), saltSource.getSalt( user ) ); */
		user.setPassword( "no password" );
	
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
	
	public void recoverPassword(User user) throws Exception
	{
		User userValid = this.findUserByEmail(user.getEmail());
		
		if(userValid == null){
			throw new Exception();
		}
		
		String s = userValid.getEmail() + Calendar.getInstance().getTime();
		MessageDigest m=MessageDigest.getInstance("MD5");
		m.update(s.getBytes(),0,s.length());
		userValid.setNewPassword(new BigInteger(1,m.digest()).toString(16).substring(0, 6));
		
		final String encodedPassword = this.passwordEncoder.encodePassword( userValid.getNewPassword(), saltSource.getSalt( userValid ) ); 
		userValid.setPassword( encodedPassword );
		
		this.userRepository.save(userValid);
		
		this.accountMailRepository.sendRecoveryPassword( userValid );
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
	
	/**
	 * 
	 * @param credentials This value must be base64 encoded, following the pattern: "email:password"
	 * @return
	 */
	@Transactional(readOnly = true)
	public User checkCredentials( String credentials )
	{
		try 
		{
			credentials = new String( Base64.decode(credentials.getBytes()) );
			final String email = credentials.substring(0, credentials.indexOf(':'));
			final String password = credentials.substring(credentials.indexOf(':')+1);
			
			final User user = (User) this.userRepository.findByEmail( email );
			final String encodedPassword = this.passwordEncoder.encodePassword(password, saltSource.getSalt(user) );
			
			if ( !user.getPassword().equals(encodedPassword) )
			{
				throw new SecurityException("Email and/or password is invalid.");
			}
			return user;
		} 
		catch ( Exception e ) 
		{
			throw new SecurityException("Email and/or password is invalid.");
		}
	}
	
	public User insertUserSocial(String email, String name)
	{
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setRole(UserRole.USER);
		user.setEnabled(true);
		user.setPassword("none");
		user = this.insertUser(user);
		return user;
	}	
	
}
