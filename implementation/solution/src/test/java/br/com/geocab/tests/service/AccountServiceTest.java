package br.com.geocab.tests.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.service.AccountService;
import br.com.geocab.tests.AbstractIntegrationTest;

/**
 *
 * @author Cristiano Correa
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
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/AccessGroupDataSet.xml"
	})
	public void insertUser()
	{
		this.authenticate(100L);
		
		User user = new User();
		user.setEmail("someUser@geocab.com.br");
		user.setName("Testing User");
		user.setPassword("123");
		user.setRole(UserRole.ADMINISTRATOR);
		user.setEnabled(true);
		
		user = this.accountService.insertUser( user );
		
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getId() );
		Assert.assertNotNull( user.getCreated() );
		
		Assert.assertEquals("someUser@geocab.com.br", user.getEmail());
		Assert.assertEquals("Testing User", user.getName());
		Assert.assertEquals(UserRole.ADMINISTRATOR, user.getRole());
		
		Assert.assertTrue(user.isEnabled());
		
		final String encodedPassword = this.passwordEncoder.encodePassword( "123" , saltSource.getSalt( user ) ); 
		Assert.assertEquals(encodedPassword , user.getPassword());
		
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml"
	})
	public void updateUser()
	{
		this.authenticate(100L);
		
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
		Assert.assertEquals(UserRole.USER, user.getRole());
		
		final String encodedPassword = this.passwordEncoder.encodePassword( "1234" , saltSource.getSalt( user ) ); 
		Assert.assertEquals(encodedPassword , user.getPassword());
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml"
	})
	public void findUserById()
	{
		this.authenticate(100L);
		
		User user = this.accountService.findUserById(100L);
		
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getId() );
		Assert.assertEquals("user@geocab.com.br", user.getEmail());
		Assert.assertEquals("Testing User", user.getName());
		Assert.assertEquals(UserRole.ADMINISTRATOR, user.getRole());
		
		Assert.assertTrue(user.isEnabled());
		
		final String encodedPassword = this.passwordEncoder.encodePassword( "123", saltSource.getSalt( user ) ); 
		Assert.assertEquals(encodedPassword , user.getPassword());
	}
	
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml"
	})
	public void findUserByEmail()
	{
		this.authenticate(100L);
		
		User user = this.accountService.findUserByEmail("user@geocab.com.br");
		
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getId() );
		Assert.assertEquals("user@geocab.com.br", user.getEmail());
		Assert.assertEquals("Testing User", user.getName());
		Assert.assertEquals(UserRole.ADMINISTRATOR, user.getRole());
		
		Assert.assertTrue(user.isEnabled());
		
		final String encodedPassword = this.passwordEncoder.encodePassword( "123", saltSource.getSalt( user ) ); 
		Assert.assertEquals(encodedPassword , user.getPassword());
	}
	
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml"
	})
	public void disableEnableUser()
	{
		this.authenticate(100L);
		
		this.accountService.disableUser(100L);
		User user = this.accountService.findUserById(100L);
		Assert.assertFalse(user.isEnabled());
		
		this.accountService.enableUser(100L);
		User user2 = this.accountService.findUserById(100L);
		Assert.assertTrue(user2.isEnabled());
	}
	
}
