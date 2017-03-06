/**
 *
 */
package br.gov.itaipu.geocab.tests.service;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.entity.datasource.ServiceType;
import br.gov.itaipu.geocab.domain.entity.layer.ExternalLayer;
import br.gov.itaipu.geocab.domain.entity.layer.Layer;
import br.gov.itaipu.geocab.domain.entity.layer.LayerField;
import br.gov.itaipu.geocab.domain.entity.layer.LayerGroup;
import br.gov.itaipu.geocab.domain.service.LayerGroupService;
import br.gov.itaipu.geocab.tests.AbstractIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Lucas
 */
public class LayerGroupServiceTest extends AbstractIntegrationTest {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

    @Value("${geoserver.wms.url}")
    private String wmsUrl;

    @Value("${geoserver.wfs.url}")
    private String wfsUrl;

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
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/AccountDataSet.xml"
    })
    public void insertLayerGroup() {
        this.authenticate(100L);

        LayerGroup layerGroup = new LayerGroup();

        Assert.assertNull(layerGroup.getId());

        layerGroup.setName("Group 1");
        layerGroup.setLayerGroupUpper(null);
        layerGroup.setOrderLayerGroup(0);
        layerGroup.setLayers(null);
        layerGroup.setLayersGroup(null);

        layerGroup = layerGroupService.insertLayerGroup(layerGroup);

        assertNotNull(layerGroup);
        Assert.assertEquals("Group 1", layerGroup.getName());
        Assert.assertEquals(null, layerGroup.getLayerGroupUpper());
        Assert.assertEquals(new Integer(0), layerGroup.getOrderLayerGroup());
        Assert.assertEquals(new ArrayList<>(), layerGroup.getLayers());
        Assert.assertEquals(null, layerGroup.getLayersGroup());

    }

    /**
     * @throws Exception
     */
    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/LayerGroupDataSet.xml",
            "/dataset/AccountDataSet.xml"
    })
    public void updateLayerGroup() throws Exception {
        this.authenticate(100L);

        LayerGroup layerGroup = this.layerGroupService.findLayerGroupById(100L);

        layerGroup.setName("Group 10 updated");
        layerGroup.setOrderLayerGroup(0);
        layerGroup.setLayers(null);
        layerGroup.setLayersGroup(null);

        layerGroup = layerGroupService.updateLayerGroup(layerGroup);

        assertNotNull(layerGroup);
        Assert.assertEquals("Group 10 updated", layerGroup.getName());
        Assert.assertEquals(new ArrayList<>(), layerGroup.getLayers());
        Assert.assertEquals(null, layerGroup.getLayersGroup());


    }

    /**
     *
     */
    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/LayerGroupDataSet.xml",
            "/dataset/AccountDataSet.xml"
    })
    public void findLayersGroupById() throws Exception {
        this.authenticate(100L);

        LayerGroup layerGroup = this.layerGroupService.findLayerGroupById(100L);

        assertNotNull(layerGroup);
        Assert.assertTrue(layerGroup.getId().equals(100L));

    }

    /**
     *
     */
    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/LayerGroupDataSet.xml",
            "/dataset/AccountDataSet.xml"
    })
    public void listLayerGroup() throws Exception {
        this.authenticate(100L);

        List<LayerGroup> layerGroups = this.layerGroupService.listLayerGroups(null, null).getContent();
        assertNotNull(layerGroups);
        Assert.assertTrue(layerGroups.size() > 0);
    }

    /**
     *
     */
    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/LayerGroupDataSet.xml",
            "/dataset/AccountDataSet.xml"
    })
    public void removeLayerGroups() {
        this.authenticate(100L);

        this.layerGroupService.removeLayerGroup(100L);
    }

    /**
     *
     */
    @Test
    public void testGetLayerFields() throws Exception {

        DataSource dataSource = new DataSource();
        dataSource.setUrl(this.wmsUrl);
        dataSource.setServiceType(ServiceType.WMS);

        // pega a primeira camada da fonte de dados para listar os seus atributos
        List<ExternalLayer> externalLayers = this.layerGroupService.getExternalLayers(dataSource);

        Layer layer = new Layer();
        layer.setName(externalLayers.get(0).getName());
        layer.setDataSource(dataSource);

        List<LayerField> layerFields = this.layerGroupService.getLayerFields(layer);

        // deve possuir mais de um campo
        assertNotNull(layerFields);
        assertTrue(!layerFields.isEmpty());
    }

    private void checkExternalLayers(List<ExternalLayer> externalLayers) {

        // checa se a lista existe
        assertNotNull(externalLayers);

        // verifica se o nome da camada está ajustado
        for (ExternalLayer externalLayer : externalLayers) {
            assertFalse(StringUtils.isEmpty(externalLayer.getName()));
        }
    }

    @Test
    public void testGetExternalLayers() throws Exception {
        // testa com wms
        DataSource wmsDataSource = new DataSource();
        wmsDataSource.setUrl(this.wmsUrl);
        wmsDataSource.setServiceType(ServiceType.WMS);

        List<ExternalLayer> wmsExternalLayers = this.layerGroupService.getExternalLayers(wmsDataSource);

        this.checkExternalLayers(wmsExternalLayers);

        // testa com wfs
        DataSource wfsDataSource = new DataSource();
        wfsDataSource.setUrl(this.wfsUrl);
        wfsDataSource.setServiceType(ServiceType.WFS);

        List<ExternalLayer> wfsExternalLayers = this.layerGroupService.getExternalLayers(wfsDataSource);

        this.checkExternalLayers(wfsExternalLayers);

        // as duas listas deverão ser as mesmas se o servidor for o mesmo
        assertEquals(wfsExternalLayers.size(), wmsExternalLayers.size());
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.CLEAN_INSERT, value = {
            "/dataset/LayerGroupDataSet.xml",
            "/dataset/AccountDataSet.xml"
    })
    public void changeLayerGroupUpper() {
        this.authenticate(100L);

        LayerGroup layerGroup = this.layerGroupService.findLayerGroupById(100L);

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

        assertNotNull(layerGroup.getLayerGroupUpper());

        Assert.assertEquals("Group 1 new Upper", layerGroup.getLayerGroupUpper().getName());


    }


}
























