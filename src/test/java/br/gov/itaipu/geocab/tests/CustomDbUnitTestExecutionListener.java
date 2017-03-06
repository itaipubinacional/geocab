package br.gov.itaipu.geocab.tests;

import com.github.springtestdbunit.DatabaseConnections;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.springframework.test.context.TestContext;

/**
 * @author rodrigo
 * @version 1.0
 * @category
 * @since 20/08/2013
 */
public class CustomDbUnitTestExecutionListener extends DbUnitTestExecutionListener {
    /**
     *
     */
    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        super.prepareTestInstance(testContext);

        DatabaseConnections connections = (DatabaseConnections) testContext.getAttribute(CONNECTION_ATTRIBUTE);
        IDatabaseConnection databaseConnection = connections.get("dataSource");

        databaseConnection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
    }
}

