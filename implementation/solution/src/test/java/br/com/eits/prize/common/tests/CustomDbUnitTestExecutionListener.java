package br.com.eits.prize.common.tests;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.springframework.test.context.TestContext;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

/**
 *
 * @author rodrigo
 * @since 20/08/2013
 * @version 1.0
 * @category
 */
public class CustomDbUnitTestExecutionListener extends DbUnitTestExecutionListener
{
	/**
	 * 
	 */
	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		super.prepareTestInstance(testContext);
		
		final IDatabaseConnection databaseConnection = (IDatabaseConnection) testContext.getAttribute(CONNECTION_ATTRIBUTE);
		
		databaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
	}
}

