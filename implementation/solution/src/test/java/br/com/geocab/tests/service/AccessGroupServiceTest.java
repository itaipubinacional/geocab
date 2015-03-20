package br.com.geocab.tests.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import br.com.geocab.domain.entity.accessgroup.AccessGroup;
import br.com.geocab.domain.entity.accessgroup.AccessGroupLayer;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.layer.CustomSearch;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.layer.LayerField;
import br.com.geocab.domain.service.AccessGroupService;
import br.com.geocab.domain.service.AccountService;
import br.com.geocab.domain.service.CustomSearchService;
import br.com.geocab.domain.service.LayerGroupService;
import br.com.geocab.tests.AbstractIntegrationTest;

/**
 *
 * @author Thiago Rossetto
 * @since 20/03/2015
 * @version 1.0
 * @category
 */
public class AccessGroupServiceTest extends AbstractIntegrationTest
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * User Repository
	 */
	@Autowired
	public AccountService accountService;
	
	/**
	 * Password encoder
	 */
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	
	/**
	 * Hash generator for encryption
	 */
	@Autowired
	private SaltSource saltSource;
	
	/**
	 * Custom Search Service
	 */
	@Autowired
	public AccessGroupService accessGroupService;
	
	/**
	 * Layer Group
	 */
	@Autowired
	private LayerGroupService layerGroupService;
	
	/**
	 * Layer Group
	 */
	@Autowired
	private CustomSearchService customSearchService;
	
	
	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/

	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/AccessGroupDataSet.xml"
	})
	public void insertAccessGroup()
	{
		this.authenticate(100L);
		
		AccessGroup accessGroup = new AccessGroup();
		accessGroup.setName("Grupo de acesso 02");
		accessGroup.setDescription("Descrição 02");
		
		accessGroup= this.accessGroupService.insertAccessGroup(accessGroup);
		
		
		Assert.assertNotNull( accessGroup );
		Assert.assertNotNull( accessGroup.getId() );
		Assert.assertNotNull( accessGroup.getCreated() );
		
		Assert.assertEquals("Grupo de acesso 02", accessGroup.getName());	
		Assert.assertEquals("Descrição 02", accessGroup.getDescription());		
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/AccessGroupDataSet.xml"
	})
	public void updateAccessGroup() throws Exception
	{
		this.authenticate(100L);
		
		AccessGroup accessGroup = this.accessGroupService.findAccessGroupById(100L);
		
		accessGroup.setName("Grupo de acesso 03");
		accessGroup.setDescription("Descrição 03");

		accessGroup = this.accessGroupService.updateAccessGroup(accessGroup);
		
		Assert.assertNotNull( accessGroup );
		Assert.assertNotNull( accessGroup .getId() );
		Assert.assertEquals("Grupo de acesso 03", accessGroup.getName());
		Assert.assertEquals("Descrição 03", accessGroup.getDescription());
		
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/AccessGroupDataSet.xml"
	})
	public void findAccessGroupById()
	{
		this.authenticate(100L);
		
		AccessGroup accessGroup = this.accessGroupService.findAccessGroupById(100L);
		
		Assert.assertNotNull( accessGroup );
		Assert.assertNotNull( accessGroup.getId() );
		Assert.assertEquals("Grupo de acesso 01", accessGroup.getName());
		Assert.assertEquals("Descrição", accessGroup.getDescription());
		
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/AccessGroupDataSet.xml"
	})
	public void removeAccessGroup()
	{	
		this.authenticate(100L);
		
		this.accessGroupService.removeAccessGroup(100L);
	}
	
	
	
}
