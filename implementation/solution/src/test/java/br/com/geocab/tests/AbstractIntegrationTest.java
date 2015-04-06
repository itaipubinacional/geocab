package br.com.geocab.tests;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.repository.account.IUserRepository;
import br.com.geocab.domain.service.DataSourceService;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * @author Rodrigo P. Fraga
 * @since 23/11/2012
 * @version 1.0
 * @category Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
	{
		"file:src/main/webapp/WEB-INF/spring/geocab-context.xml",
	}
)
@DatabaseSetup(value="/dataset/AbstractDataSet.xml", type=DatabaseOperation.DELETE_ALL)
@TestExecutionListeners
(
	{ 
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		ServletTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class,
		CustomDbUnitTestExecutionListener.class
	}
)
//@DatabaseSetup(value="/dataset/AbstractDataSet.xml", type=DatabaseOperation.DELETE_ALL)
public abstract class AbstractIntegrationTest
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	@Autowired
	private IUserRepository userRepository;
	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Before
	public void setUp()
	{
	}
	
	protected void authenticate( long userId )
	{
		   final User user = this.userRepository.findOne( userId );
		   final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken( user, null, user.getAuthorities() );
	       SecurityContextHolder.createEmptyContext();
	       SecurityContextHolder.getContext().setAuthentication( token );
	}
}