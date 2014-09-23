package br.com.geocab.tests.common;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

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
}