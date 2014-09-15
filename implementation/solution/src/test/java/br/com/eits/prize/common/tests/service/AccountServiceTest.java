package br.com.eits.prize.common.tests.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.eits.prize.common.tests.AbstractIntegrationTest;
import br.com.geocab.domain.entity.account.User;
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
		this.accountService.insertUser( user );
		
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getId() );
		Assert.assertNotNull( user.getCreated() );
	}
}
