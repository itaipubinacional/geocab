/**
 * 
 */
package br.com.geocab.tests.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.service.DataSourceService;
import br.com.geocab.tests.common.AbstractIntegrationTest;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;


/**
 * @author Lucas
 *
 */

public class DataSourceServiceTest extends AbstractIntegrationTest
{
	@Autowired
	private DataSourceService dataSourceService;

	@Test
	public void insertDataSouce()
	{
		DataSource dataSource = new DataSource();
		
		dataSource.setName("Data Source");
		dataSource.setLogin("user");
		dataSource.setPassword("password123");
		dataSource.setInternal(true);
		dataSource.setUrl("url1");
	
		
		dataSource = dataSourceService.insertDataSource(dataSource);
		
		Assert.assertNotNull(dataSource);
		Assert.assertEquals("Data Source", dataSource.getName());
		Assert.assertEquals("user", dataSource.getLogin());
		Assert.assertEquals("password123", dataSource.getPassword());
		Assert.assertEquals("url1", dataSource.getUrl());
		Assert.assertTrue(dataSource.getInternal());
			
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/DataSourceDataSet.xml"
	})
	public void updateDataSource() throws Exception
	{
		DataSource dataSource = dataSourceService.findDataSourceById(100L);
		
		dataSource.setName("Data Source changed");
		dataSource.setLogin("user changed");
		dataSource.setPassword("password123 changed");
		dataSource.setUrl("url1 changed");
		dataSource.setInternal(false);
		
		dataSource = dataSourceService.updateDataSource(dataSource);
		
		
		Assert.assertNotNull(dataSource);
		Assert.assertEquals("Data Source changed", dataSource.getName());
		Assert.assertEquals("user changed", dataSource.getLogin());
		Assert.assertEquals("password123 changed", dataSource.getPassword());
		Assert.assertEquals("url1 changed", dataSource.getUrl());
		Assert.assertFalse(dataSource.getInternal());
		
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/DataSourceDataSet.xml"
	})
	public void findFonteDadosById() throws Exception
	{
		
		DataSource dataSource = dataSourceService.findDataSourceById(100L);
		Assert.assertNotNull(dataSource);
		Assert.assertTrue(dataSource.getId().equals(100L));
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/DataSourceDataSet.xml"
	})
	public void removeDataSource()
	{
		this.dataSourceService.removeDataSource(100L);
	}
	
	
}













