package br.gov.itaipu.geocab.tests;

import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.repository.configuration.account.UserRepository;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;


/**
 * @author Rodrigo P. Fraga
 * @version 1.0
 * @category Test
 * @since 23/11/2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseSetup(value = "/dataset/AbstractDataSet.xml", type = DatabaseOperation.DELETE_ALL)
@TestExecutionListeners({
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
        ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        CustomDbUnitTestExecutionListener.class
})
@SpringBootTest
public abstract class AbstractIntegrationTest {
    /*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/

    @Autowired
    private UserRepository userRepository;

	/*-------------------------------------------------------------------
     *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    @Before
    public void setUp() {
    }

    protected void authenticate(long userId) {
        final User user = this.userRepository.findOne(userId);
        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}