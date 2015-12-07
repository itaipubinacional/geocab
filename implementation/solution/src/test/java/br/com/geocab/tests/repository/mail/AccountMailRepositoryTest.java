package br.com.geocab.tests.repository.mail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.repository.IAccountMailRepository;
import br.com.geocab.tests.AbstractIntegrationTest;

/**
 * 
 * @author rodrigo
 * @since 09/05/2013
 * @version 1.0
 * @category
 
 */
public class AccountMailRepositoryTest extends AbstractIntegrationTest
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/	
	/**
	 * 
	 */
	@Autowired
	private IAccountMailRepository accountMailRepository;

	/*-------------------------------------------------------------------
	 *				 		     TESTS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Test
	public void sendRecoveryPassword()
	{
		final User user = new User();
		user.setEmail("thiago.rossetto@eits.com.br");
		user.setName("Thiago Rossetttttttto");
		
		this.accountMailRepository.sendRecoveryPassword( user );
	}
}
