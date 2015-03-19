/**
 * 
 */
package br.com.geocab.tests.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.layer.LayerGroup;
import br.com.geocab.domain.service.LayerGroupService;
import br.com.geocab.tests.AbstractIntegrationTest;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * @author Lucas
 *
 */
public class LayerGroupServiceTest extends AbstractIntegrationTest
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private LayerGroupService layerGroupService;
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml"
	})
	public void insertLayerGroup()
	{
		this.authenticate(100L);
		
		LayerGroup layerGroup = new LayerGroup();
		
		Assert.assertNull(layerGroup.getId());
		
		layerGroup.setName("Group 1");
		layerGroup.setLayerGroupUpper(null);
		layerGroup.setOrderLayerGroup(0);
		layerGroup.setLayers(null);
		layerGroup.setLayersGroup(null);
		
		layerGroup = layerGroupService.insertLayerGroup(layerGroup);
		
		Assert.assertNotNull(layerGroup);
		Assert.assertEquals("Group 1", layerGroup.getName());
		Assert.assertEquals(null, layerGroup.getLayerGroupUpper());
		Assert.assertEquals(0, layerGroup.getOrderLayerGroup());
		Assert.assertEquals(null, layerGroup.getLayers());
		Assert.assertEquals(null, layerGroup.getLayersGroup());
		
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/LayerGroupDataSet.xml",
			"/dataset/AccountDataSet.xml"
	})
	public void updateLayerGroup() throws Exception
	{
		this.authenticate(100L);
		
		LayerGroup layerGroup = this.layerGroupService.findLayerGroupById(100L);
		
		layerGroup.setName("Group 10 updated");
		layerGroup.setOrderLayerGroup(0);
		layerGroup.setLayers(null);
		layerGroup.setLayersGroup(null);
		
		layerGroup = layerGroupService.updateLayerGroup(layerGroup);
		
		Assert.assertNotNull(layerGroup);
		Assert.assertEquals("Group 10 updated", layerGroup.getName());
		Assert.assertEquals(null, layerGroup.getLayers());
		Assert.assertEquals(null, layerGroup.getLayersGroup());
		
		
	}
	/**
	 * 
	 */
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/LayerGroupDataSet.xml",
			"/dataset/AccountDataSet.xml"
	})
	public void findLayersGroupById() throws Exception
	{
		this.authenticate(100L);
		
		LayerGroup layerGroup = this.layerGroupService.findLayerGroupById(100L);
		
		Assert.assertNotNull(layerGroup);
		Assert.assertTrue(layerGroup.getId().equals(100L));
		
	}
	/**
	 * 
	 */
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/LayerGroupDataSet.xml",
			"/dataset/AccountDataSet.xml"
	})
	public void listLayerGroup() throws Exception
	{
		this.authenticate(100L);
		
		List<LayerGroup> layerGroups = this.layerGroupService.listLayerGroups(null, null).getContent();
		Assert.assertNotNull(layerGroups);
		Assert.assertTrue(layerGroups.size() > 0);
	}
	
	/**
	 * 
	 */
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/LayerGroupDataSet.xml",
			"/dataset/AccountDataSet.xml"
	})
	public void removeLayerGroups()
	{
		this.authenticate(100L);
		
		this.layerGroupService.removeLayerGroup(100L);
	}
	
	/**
	 * 
	 */
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml"
	})
	public void listLayerGroupsByFilter() throws JAXBException
	{
		this.authenticate(100L);
		
		DataSource dataSource = new DataSource();
		dataSource.setUrl("http://geocab.sbox.me/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities");
		
		Layer layer = new Layer();
		layer.setName("Name");
		layer.setDataSource(dataSource);
		
		this.layerGroupService.listFieldLayersByFilter(layer);
		
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/LayerGroupDataSet.xml",
			"/dataset/AccountDataSet.xml"
	})
	public void changeLayerGroupUpper()
	{
		this.authenticate(100L);
		
		LayerGroup layerGroup= this.layerGroupService.findLayerGroupById(100L);
		
		LayerGroup layerGroupUpper = new LayerGroup();
		
		layerGroupUpper.setName("Group 1 new Upper");
		layerGroupUpper.setLayerGroupUpper(null);
		layerGroupUpper.setOrderLayerGroup(0);
		layerGroupUpper.setLayers(null);
		layerGroupUpper.setLayersGroup(null);
		
		List<LayerGroup> groups = new ArrayList<LayerGroup>();
		groups.add(layerGroup);
		
		layerGroupUpper = this.layerGroupService.insertLayerGroup(layerGroupUpper);
		
		layerGroup.setLayerGroupUpper(layerGroupUpper);
		
		layerGroupUpper.setLayersGroup(groups);
		
		layerGroup = this.layerGroupService.updateLayerGroup(layerGroup);
		
		layerGroupUpper = this.layerGroupService.updateLayerGroup(layerGroupUpper);
		
		Assert.assertNotNull(layerGroup.getLayerGroupUpper());

		Assert.assertEquals("Group 1 new Upper", layerGroup.getLayerGroupUpper().getName());
		
		
	}

	
}
























