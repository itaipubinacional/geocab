/**
 *
 */
package br.gov.itaipu.geocab.tests.service;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.service.DataSourceService;
import br.gov.itaipu.geocab.tests.AbstractIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Lucas
 */

public class DataSourceServiceTest extends AbstractIntegrationTest {
    @Autowired
    private DataSourceService dataSourceService;

    @Test
    @DatabaseSetup(type = DatabaseOperation.INSERT, value = {
            "/dataset/AccountDataSet.xml"
    })
    public void insertDataSouce() {
        this.authenticate(100L);

        DataSource dataSource = new DataSource();

        dataSource.setName("Data Source");
        dataSource.setLogin("user");
        dataSource.setPassword("password123");
//		dataSource.setInternal(true);
        dataSource.setUrl("http://geocab.sbox.me/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities");


        dataSource = dataSourceService.insertDataSource(dataSource);

        Assert.assertNotNull(dataSource);
        Assert.assertEquals("Data Source", dataSource.getName());
        Assert.assertEquals("user", dataSource.getLogin());
        Assert.assertEquals("password123", dataSource.getPassword());
        Assert.assertEquals("http://geocab.sbox.me/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities&authkey=2da2ca323d28628d05151b9070e27da0", dataSource.getUrl());
//		Assert.assertTrue(dataSource.getInternal());

    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.INSERT, value = {
            "/dataset/DataSourceDataSet.xml",
            "/dataset/AccountDataSet.xml"
    })
    public void updateDataSource() throws Exception {
        this.authenticate(100L);

        DataSource dataSource = dataSourceService.findDataSourceById(100L);

        dataSource.setName("Data Source changed");
        dataSource.setLogin("user changed");
        dataSource.setPassword("password123 changed");
        dataSource.setUrl("http://geocab.sbox.me/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities");
//		dataSource.setInternal(false);

        dataSource = dataSourceService.updateDataSource(dataSource);


        Assert.assertNotNull(dataSource);
        Assert.assertEquals("Data Source changed", dataSource.getName());
        Assert.assertEquals("user changed", dataSource.getLogin());
        Assert.assertEquals("password123 changed", dataSource.getPassword());
        Assert.assertEquals("http://geocab.sbox.me/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities&authkey=5d8101ca1d34c56858a5fc7fae979527", dataSource.getUrl());
//		Assert.assertFalse(dataSource.getInternal());

    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.INSERT, value = {
            "/dataset/DataSourceDataSet.xml",
            "/dataset/AccountDataSet.xml"
    })
    public void findFonteDadosById() throws Exception {
        this.authenticate(100L);

        DataSource dataSource = dataSourceService.findDataSourceById(100L);
        Assert.assertNotNull(dataSource);
        Assert.assertTrue(dataSource.getId().equals(100L));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.INSERT, value = {
            "/dataset/DataSourceDataSet.xml",
            "/dataset/AccountDataSet.xml"
    })
    public void removeDataSource() {
        this.authenticate(100L);

        this.dataSourceService.removeDataSource(100L);
    }


}













