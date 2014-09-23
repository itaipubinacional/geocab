package br.com.geocab.tests.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.service.AccountService;
import br.com.geocab.tests.common.AbstractIntegrationTest;

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
	 *
	 */
	@Autowired
	public AccountService accountService;
	
	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Test
	public void insertUser()
	{
		final User user = new User();
		user.setEmail("test@geocab.com.br");
		user.setName("Testing User");
		user.setPassword("admin");
		user.setRole(UserRole.ADMINISTRATOR);
		this.accountService.insertUser( user );
		
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getId() );
		Assert.assertNotNull( user.getCreated() );
		
	}
}
