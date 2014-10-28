package br.com.geocab.application.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.domain.service.MarkerService;


/**
 * @author Vinicius Ramos Kawamoto
 * @since 15/10/2014
 * @version 1.0
 * @category Controller
 */

@Controller
@RequestMapping("marker")
public class MarkerRESTful
{
	
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private MarkerService markerService;
	
	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 * @param layerId
	 * @return
	 */
	@RequestMapping(value="/{layerId}/markers", method = RequestMethod.GET)
	public @ResponseBody List<Marker> listMarkerByLayer(@PathVariable long layerId)
	{
		return this.markerService.listMarkerByLayer(layerId);
	}
	
	/**
	 * 
	 * @param layerId
	 * @return
	 */
	@RequestMapping(value="/{markerId}/markerattributes", method = RequestMethod.GET)
	public @ResponseBody List<MarkerAttribute> listAttributeByMarker(@PathVariable long markerId)
	{
		return this.markerService.listAttributeByMarker(markerId);
	}
	
}