package br.com.geocab.application.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.service.LayerGroupService;


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
	private LayerGroupService layerGroupService;
	
	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 * @param layerId
	 * @return
	 */
	@RequestMapping(value="/layers", method = RequestMethod.GET)
	public @ResponseBody List<Layer> listAllInternalLayers()
	{
		return this.layerGroupService.listAllInternalLayers();	
	}
	
	
}