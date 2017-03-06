package br.gov.itaipu.geocab.tests.repository.mail;

import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.infrastructure.mail.AccountMailRepository;
import br.gov.itaipu.geocab.tests.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author rodrigo
 * @version 1.0
 * @category
 * @since 09/05/2013
 */
public class AccountMailRepositoryTest extends AbstractIntegrationTest {
    /*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    @Autowired
    private AccountMailRepository accountMailRepository;

	/*-------------------------------------------------------------------
	 *				 		     TESTS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    @Test
    public void sendRecoveryPassword() {
        final User user = new User();
        user.setEmail("test_prognus@mailinator.com");
        user.setName("Test Prognus");

        this.accountMailRepository.sendRecoveryPassword(user);
    }
}
