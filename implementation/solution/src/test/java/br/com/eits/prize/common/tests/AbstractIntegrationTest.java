package br.com.eits.prize.common.tests;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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