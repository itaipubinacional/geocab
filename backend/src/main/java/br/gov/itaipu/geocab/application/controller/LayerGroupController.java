package br.gov.itaipu.geocab.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    	
        return this.layerGroupService.listLayerGroups(null, null).getContent();    	
    }
}