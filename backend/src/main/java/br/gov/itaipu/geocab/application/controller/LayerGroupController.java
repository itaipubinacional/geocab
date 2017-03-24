package br.gov.itaipu.geocab.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    	return this.layerGroupService.updateLayerGroup(layerGroup);
    	
    }
    
    @RequestMapping(method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('admin')")
    public @ResponseBody ResponseEntity removeLayerGroup(@RequestBody LayerGroup layerGroup) {
    	this.layerGroupService.removeLayerGroup(layerGroup.getId());
		return null;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @PreAuthorize("hasRole('admin')")
    public
    @ResponseBody LayerGroup getLayerGroup(@PathVariable long id) {
        return this.layerGroupService.findLayerGroupById(id);
    }
}
