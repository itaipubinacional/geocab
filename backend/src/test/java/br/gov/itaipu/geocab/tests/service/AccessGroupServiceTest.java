package br.gov.itaipu.geocab.tests.service;

import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroup;
import br.gov.itaipu.geocab.domain.service.AccessGroupService;
import br.gov.itaipu.geocab.domain.service.AccountService;
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
public class AccessGroupServiceTest extends AbstractIntegrationTest {
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
    public AccessGroupService accessGroupService;
	
	
	/*-------------------------------------------------------------------
	 *				 		     	TESTS
	 *-------------------------------------------------------------------*/

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/AccessGroupDataSet.xml"
    })
    public void insertAccessGroup() {
        this.authenticate(100L);

        AccessGroup accessGroup = this.accessGroupService.findAccessGroupById(1L);
        accessGroup.setName("Grupo de acesso 02");
        accessGroup.setDescription("Descrição 02");

        accessGroup = this.accessGroupService.insertAccessGroup(accessGroup);


        Assert.assertNotNull(accessGroup);
        Assert.assertNotNull(accessGroup.getId());
        Assert.assertNotNull(accessGroup.getCreated());

        Assert.assertEquals("Grupo de acesso 02", accessGroup.getName());
        Assert.assertEquals("Descrição 02", accessGroup.getDescription());
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/AccessGroupDataSet.xml"
    })
    public void updateAccessGroup() throws Exception {
        this.authenticate(100L);

        AccessGroup accessGroup = this.accessGroupService.findAccessGroupById(100L);

        accessGroup.setName("Grupo de acesso 03");
        accessGroup.setDescription("Descrição 03");

        accessGroup = this.accessGroupService.updateAccessGroup(accessGroup);

        Assert.assertNotNull(accessGroup);
        Assert.assertNotNull(accessGroup.getId());
        Assert.assertEquals("Grupo de acesso 03", accessGroup.getName());
        Assert.assertEquals("Descrição 03", accessGroup.getDescription());

    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/AccessGroupDataSet.xml"
    })
    public void findAccessGroupById() {
        this.authenticate(100L);

        AccessGroup accessGroup = this.accessGroupService.findAccessGroupById(100L);

        Assert.assertNotNull(accessGroup);
        Assert.assertNotNull(accessGroup.getId());
        Assert.assertEquals("Grupo de acesso 01", accessGroup.getName());
        Assert.assertEquals("Descrição", accessGroup.getDescription());

    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/AccessGroupDataSet.xml"
    })
    public void removeAccessGroup() {
        this.authenticate(100L);

        this.accessGroupService.removeAccessGroup(100L);
    }


}
