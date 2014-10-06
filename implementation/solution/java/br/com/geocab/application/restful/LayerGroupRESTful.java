package br.com.geocab.application.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.geocab.domain.entity.layer.LayerGroup;
import br.com.geocab.domain.service.LayerGroupService;


/**
 * @author Vinicius Ramos Kawamoto
 * @since 25/09/2014
 * @version 1.0
 * @category Controller
 */

@Controller
@RequestMapping("layergroup")
public class LayerGroupRESTful
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
	 * @return
	 */
	@RequestMapping(value="layers", method = RequestMethod.GET)
	public @ResponseBody List<LayerGroup> listLayerGroups()
	{
		List<LayerGroup> layerGroups = this.layerGroupService.listLayerGroupUpperPublished();
		
		return layerGroups;
	}
	
}