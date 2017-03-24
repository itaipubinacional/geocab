package br.gov.itaipu.geocab.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.entity.layer.LayerGroup;
import br.gov.itaipu.geocab.domain.service.LayerGroupService;

@RestController
@RequestMapping("/api/layer-group")
public class LayerGroupController {
	
	@Autowired
	private LayerGroupService layerGroupService;
	
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('admin')")
    public @ResponseBody List<LayerGroup> getLayerGroups() {
    	
    	return this.layerGroupService.listLayersGroupUpper();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('admin')")
    public @ResponseBody LayerGroup createLayerGroup(@RequestBody LayerGroup layerGroup) {
    	return this.layerGroupService.insertLayerGroup(layerGroup);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('admin')")
    public @ResponseBody LayerGroup updateLayerGroup(@RequestBody LayerGroup layerGroup) {    	    	
    	LayerGroup lg =  this.layerGroupService.updateLayerGroup(layerGroup);
    	return this.layerGroupService.findLayerGroupById(lg.getId());
    	
    	
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @PreAuthorize("hasRole('admin')")
    public @ResponseBody ResponseEntity removeLayerGroup(@PathVariable long id) {
    	this.layerGroupService.removeLayerGroup(id);
		return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @PreAuthorize("hasRole('admin')")
    public
    @ResponseBody LayerGroup getLayerGroup(@PathVariable long id) {
        return this.layerGroupService.findLayerGroupById(id);
    }
}
