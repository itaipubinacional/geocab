/**
 *
 */
package br.gov.itaipu.geocab.tests.service;

import br.gov.itaipu.geocab.domain.entity.marker.MarkerStatus;
import br.gov.itaipu.geocab.domain.entity.markermoderation.MarkerModeration;
import br.gov.itaipu.geocab.domain.entity.markermoderation.Motive;
import br.gov.itaipu.geocab.domain.service.MotiveService;
import br.gov.itaipu.geocab.domain.service.marker.MarkerModerationService;
import br.gov.itaipu.geocab.domain.service.marker.MarkerService;
import br.gov.itaipu.geocab.tests.AbstractIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Lucas
 */
public class MarkerModerationServiceTest extends AbstractIntegrationTest {

	/*-------------------------------------------------------------------
     *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    @Autowired
    public MarkerModerationService markerModerationService;

    /**
     *
     */
    @Autowired
    public MarkerService markerService;

    /**
     *
     */
    @Autowired
    public MotiveService motiveService;

    /*-------------------------------------------------------------------
     *				 		     	TESTS
     *-------------------------------------------------------------------*/
    @Test
    @DatabaseSetup(type = DatabaseOperation.INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/LayerConfigDataSet.xml",
            "/dataset/MarkerDataSet.xml",
            "/dataset/MarkerModerationDataSet.xml"
    })
    public void acceptMarkerModeration() {
        this.authenticate(100L);

        MarkerModeration markerModeration = this.markerModerationService.acceptMarker(100L);

        Assert.assertNotNull(markerModeration);
        Assert.assertEquals(MarkerStatus.ACCEPTED, markerModeration.getMarker().getStatus());
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.INSERT, value = {
            "/dataset/AccountDataSet.xml",
            "/dataset/LayerConfigDataSet.xml",
            "/dataset/MarkerDataSet.xml",
            "/dataset/MarkerModerationDataSet.xml",
            "/dataset/MotiveDataSet.xml"
    })
    public void refuseMarkerModeration() {
        this.authenticate(100L);

        List<Motive> motive = motiveService.listMotives();

        MarkerModeration markerModeration = this.markerModerationService.refuseMarker(100L, motive.get(0), "Test");

        Assert.assertNotNull(markerModeration);
        Assert.assertEquals(MarkerStatus.REFUSED, markerModeration.getMarker().getStatus());
    }


}


































