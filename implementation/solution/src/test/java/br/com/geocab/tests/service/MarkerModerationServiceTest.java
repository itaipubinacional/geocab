/**
 * 
 */
package br.com.geocab.tests.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.domain.entity.markermoderation.Motive;
import br.com.geocab.domain.service.MarkerModerationService;
import br.com.geocab.domain.service.MarkerService;
import br.com.geocab.domain.service.MotiveService;
import br.com.geocab.tests.AbstractIntegrationTest;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * @author Lucas
 *
 */
public class MarkerModerationServiceTest extends AbstractIntegrationTest
{
	
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
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/LayerConfigDataSet.xml",
			"/dataset/MarkerDataSet.xml",
			"/dataset/MarkerModerationDataSet.xml"
	})
	public void acceptMarkerModeration()
	{
		this.authenticate(100L);
		
		MarkerModeration markerModeration = this.markerModerationService.acceptMarker(100L);
		
		Assert.assertNotNull(markerModeration);
		Assert.assertEquals(MarkerStatus.ACCEPTED, markerModeration.getMarker().getStatus());
	}
	
	@Test
	@DatabaseSetup(type=DatabaseOperation.INSERT, value={
			"/dataset/AccountDataSet.xml",
			"/dataset/LayerConfigDataSet.xml",
			"/dataset/MarkerDataSet.xml",
			"/dataset/MarkerModerationDataSet.xml",
			"/dataset/MotiveDataSet.xml"
	})
	public void refuseMarkerModeration()
	{
		this.authenticate(100L);
		
		List<Motive> motive = motiveService.listMotives();
		
		MarkerModeration markerModeration = this.markerModerationService.refuseMarker(100L, motive.get(0), "Test");
		
		Assert.assertNotNull(markerModeration);
		Assert.assertEquals(MarkerStatus.REFUSED, markerModeration.getMarker().getStatus());		
	}
	

}


































