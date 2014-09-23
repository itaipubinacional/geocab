package br.com.eits.prize.common.tests.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import br.com.eits.prize.common.tests.AbstractIntegrationTest;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.service.AccountService;

/**
 *
 * @author rodrigo
 * @since 09/05/2013
 * @version 1.0
 * @category
 */
public class AccountServiceTest extends AbstractIntegrationTest
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * User Repository
	 */
	@Autowired
	public AccountService accountService;
	
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
	
	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/

	@Test
	public void insertUser()
	{
		User user = new User();
		user.setEmail("user@geocab.com.br");
		user.setName("Testing User");
		user.setPassword("123");
		user.setRole(UserRole.ADMINISTRATOR);
		user.setEnabled(true);
		
		user = this.accountService.insertUser( user );
		
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getId() );
		Assert.assertNotNull( user.getCreated() );
		
		Assert.assertEquals("user@geocab.com.br", user.getEmail());
		Assert.assertEquals("Testing User", user.getName());
		Assert.assertEquals("ADMINISTRATOR", user.getRole());
		
		Assert.assertTrue(user.isEnabled());
		
		final String encodedPassword = this.passwordEncoder.encodePassword( "123" , saltSource.getSalt( user ) ); 
		Assert.assertEquals(encodedPassword , user.getPassword());
		
	}
	
	@Test
	public void updateUser()
	{
		User user = this.accountService.findUserById(100L);
		
		user.setEmail("user2@geocab.com.br");
		user.setName("Testing User2");
		user.setPassword("1234");
		user.setRole(UserRole.USER);
		
		user = this.accountService.updateUser( user );
		
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getId() );
		Assert.assertEquals("user2@geocab.com.br", user.getEmail());
		Assert.assertEquals("Testing User2", user.getName());
		Assert.assertEquals("USER", user.getRole());
		
		final String encodedPassword = this.passwordEncoder.encodePassword( "1234" , saltSource.getSalt( user ) ); 
		Assert.assertEquals(encodedPassword , user.getPassword());
	}
	
	@Test
	public void findUserById()
	{
		User user = this.accountService.findUserById(100L);
		
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getId() );
		Assert.assertEquals("user@geocab.com.br", user.getEmail());
		Assert.assertEquals("Testing User", user.getName());
		Assert.assertEquals("ADMINISTRATOR", user.getRole());
		
		Assert.assertTrue(user.isEnabled());
		
		final String encodedPassword = this.passwordEncoder.encodePassword( "123", saltSource.getSalt( user ) ); 
		Assert.assertEquals(encodedPassword , user.getPassword());
	}
	
	@Test
	public void disableUser()
	{
		this.accountService.disableUser(100L);
		User user = this.accountService.findUserById(100L);
		
		Assert.assertFalse(user.isEnabled());
		
	}
	
	@Test
	public void enableUser( )
	{	
		this.accountService.enableUser(100L);
		User user = this.accountService.findUserById(100L);
		
		Assert.assertTrue(user.isEnabled());
	}
}
