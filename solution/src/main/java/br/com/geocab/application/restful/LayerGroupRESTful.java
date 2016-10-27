package br.com.geocab.application.restful;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.service.LayerGroupService;


/**
 * @author Vinicius Ramos Kawamoto
 * @since 25/09/2014
 * @version 1.0
 * @category Controller
 */

@Controller
@RequestMapping("/layergroup")
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
	@RequestMapping(value="/layers", method = RequestMethod.GET)
	public @ResponseBody List<Layer> listLayerGroups()
	{
		List<Layer> layers = this.layerGroupService.listLayersPublished();
		
		for ( Layer layer : layers )
		{
			
			if( layer.getDataSource().getUrl() != null )
			{
				int position = layer.getDataSource().getUrl().lastIndexOf("geoserver/");
				String urlGeoserver = layer.getDataSource().getUrl().substring(0, position+10);
				
				layer.setLegend( urlGeoserver + Layer.LEGEND_GRAPHIC_URL + layer.getName() + Layer.LEGEND_GRAPHIC_FORMAT );
			}
			
		}
		
		return layers;
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value="/internal/layers", method = RequestMethod.GET)
	public @ResponseBody List<Layer> listAllInternalLayerGroups()
	{
		return this.layerGroupService.listAllInternalLayerGroups();
	}	
	
	/**
	 * 
	 * @param layerId
	 * @return
	 */
	@RequestMapping(value="/{layerId}/layerattributes", method = RequestMethod.GET)
	public @ResponseBody List<Attribute> listAttributeByMarker(@PathVariable long layerId)
	{
		return this.layerGroupService.listAttributesByLayer(layerId);
	}
	
	/**
	 * 
	 * @param listUrls
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/layerproperties", method = RequestMethod.POST, produces={"application/json; charset=UTF-8"})
	public @ResponseBody List<String> listAllFeatures(@RequestBody List<DataSource> dataSources) throws UnsupportedEncodingException
	{
		List<String> listUrls = new ArrayList<>();
		
		for ( DataSource source : dataSources)
		{
			listUrls.add(source.getUrl());
		}
		
		return this.layerGroupService.listAllFeatures(listUrls);
	}	
	
	
}