/**
 *
 */
package br.gov.itaipu.geocab.tests.entity;

import br.gov.itaipu.geocab.domain.entity.configuration.account.Email;
import org.junit.Test;


/**
 * @author emanuelvictor
 */
public class EmailTest {
    /*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/


    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testEmailNotBeNull() throws Exception {
        Email email = new Email();
        email.validate();
    }

    @Test
    public void testEmailValid() throws Exception {
        Email email = new Email();
        email.setName("Name");
        email.setSubject("Subject");
        email.setMessage("Subject");
        email.setEmail("email@email");
        email.validate();
    }

    @Test(expected = java.lang.Exception.class)
    public void testEmailInvalid() throws Exception {
        Email email = new Email();
        email.setName("Name");
        email.setSubject("Subject");
        email.setMessage("Subject");
        email.setEmail("email");
        email.validate();
    }
}
