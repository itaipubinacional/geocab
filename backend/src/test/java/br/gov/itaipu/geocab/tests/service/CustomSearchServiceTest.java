package br.gov.itaipu.geocab.tests.service;

import br.gov.itaipu.geocab.domain.entity.layer.CustomSearch;
import br.gov.itaipu.geocab.domain.service.AccountService;
import br.gov.itaipu.geocab.domain.service.CustomSearchService;
import br.gov.itaipu.geocab.domain.service.LayerGroupService;
import br.gov.itaipu.geocab.tests.AbstractIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Thiago Rossetto
 * @version 1.0
 * @category
 * @since 20/03/2015
 */
public class CustomSearchServiceTest extends AbstractIntegrationTest {
    /*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     * User Repository
     */
    @Autowired
    public AccountService accountService;

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
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/LayerConfigDataSet.xml"
    })
    public void insertCustomSearch() {
        this.authenticate(100L);

        CustomSearch customSearch = new CustomSearch();
        customSearch.setLayer(this.layerGroupService.findLayerById(502L));
        customSearch.setName("Pesquisa Personalizada");

        Long customSearchId = this.customSearchService.insertCustomSearch(customSearch);

        customSearch = this.customSearchService.findCustomSearchById(customSearchId);

        Assert.assertNotNull(customSearch);
        Assert.assertNotNull(customSearch.getId());

        Assert.assertEquals("Pesquisa Personalizada", customSearch.getName());
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/LayerConfigDataSet.xml",
            "/dataset/CustomSearchDataSet.xml"
    })
    public void updateCustomSearch() throws Exception {
        this.authenticate(100L);

        CustomSearch customSearch = this.customSearchService.findCustomSearchById(1L);

        customSearch.setName("Pesquisa Personalizada 03");

        customSearch = this.customSearchService.updateCustomSearch(customSearch);

        Assert.assertNotNull(customSearch);
        Assert.assertNotNull(customSearch.getId());
        Assert.assertEquals("Pesquisa Personalizada 03", customSearch.getName());
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/LayerConfigDataSet.xml",
            "/dataset/CustomSearchDataSet.xml"
    })
    public void findCustomSearchById() {
        this.authenticate(100L);

        CustomSearch customSearch = this.customSearchService.findCustomSearchById(1L);

        Assert.assertNotNull(customSearch);
        Assert.assertNotNull(customSearch.getId());
        Assert.assertEquals("Pesquisa Personalizada 02", customSearch.getName());

    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/LayerConfigDataSet.xml",
            "/dataset/CustomSearchDataSet.xml"
    })
    public void removeLayer() {
        this.authenticate(100L);

        this.customSearchService.removeCustomSearch(1L);
    }

}
