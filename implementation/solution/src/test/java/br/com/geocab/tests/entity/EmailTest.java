/**
 * 
 */
package br.com.geocab.tests.entity;

import org.junit.Test;

import br.com.geocab.domain.entity.configuration.account.Email;

/**
 * @author emanuelvictor
 *
 */
public class EmailTest
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/

		
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testEmailNotBeNull() throws Exception
	{
		Email email = new Email();
		email.validate();
	}

	@Test
	public void testEmailValid() throws Exception
	{
		Email email = new Email();
		email.setName("Name");
		email.setSubject("Subject");
		email.setMessage("Subject");
		email.setEmail("email@email");
		email.validate();
	}
	
	@Test(expected = java.lang.Exception.class)
	public void testEmailInvalid() throws Exception
	{
		Email email = new Email();
		email.setName("Name");
		email.setSubject("Subject");
		email.setMessage("Subject");
		email.setEmail("email");
		email.validate();
	}
}
