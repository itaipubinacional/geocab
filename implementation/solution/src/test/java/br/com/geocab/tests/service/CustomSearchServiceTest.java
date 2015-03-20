package br.com.geocab.tests.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.layer.CustomSearch;
import br.com.geocab.domain.entity.layer.LayerField;
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
public class CustomSearchServiceTest extends AbstractIntegrationTest
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
	public CustomSearchService customSearchService;
	
	/**
	 * Layer Group
	 */
	@Autowired
	private LayerGroupService layerGroupService;
	
	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/

	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/LayerConfigDataSet.xml"
	})
	public void insertCustomSearch()
	{
		this.authenticate(100L);
		
		CustomSearch customSearch = new CustomSearch();
		customSearch.setLayer(this.layerGroupService.findLayerById(2L));
		customSearch.setName("Pesquisa Personalizada");

		Long customSearchId = this.customSearchService.insertCustomSearch(customSearch);
		
		customSearch = this.customSearchService.findCustomSearchById(customSearchId);
		
		Assert.assertNotNull( customSearch );
		Assert.assertNotNull( customSearch.getId() );
		Assert.assertNotNull( customSearch.getCreated() );
		
		Assert.assertEquals("Pesquisa Personalizada", customSearch.getName());		
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/LayerConfigDataSet.xml",
			"/dataset/CustomSearchDataSet.xml"
	})
	public void updateCustomSearch() throws Exception
	{
		this.authenticate(100L);
		
		CustomSearch customSearch = this.customSearchService.findCustomSearchById(1L);
		
		customSearch.setName("Pesquisa Personalizada 03");

		customSearch = this.customSearchService.updateCustomSearch(customSearch);
		
		Assert.assertNotNull( customSearch );
		Assert.assertNotNull( customSearch .getId() );
		Assert.assertEquals("Pesquisa Personalizada 03", customSearch.getName());
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/LayerConfigDataSet.xml",
			"/dataset/CustomSearchDataSet.xml"
	})
	public void findCustomSearchById()
	{
		this.authenticate(100L);
		
		CustomSearch customSearch = this.customSearchService.findCustomSearchById(1L);
		
		Assert.assertNotNull( customSearch );
		Assert.assertNotNull( customSearch.getId() );
		Assert.assertEquals("Pesquisa Personalizada 02", customSearch.getName());
		
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/LayerConfigDataSet.xml",
			"/dataset/CustomSearchDataSet.xml"
	})
	public void removeLayer()
	{	
		this.authenticate(100L);
		
		this.customSearchService.removeCustomSearch(1L);
	}
	
}
