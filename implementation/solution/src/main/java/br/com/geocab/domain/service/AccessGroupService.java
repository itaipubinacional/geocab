package br.com.geocab.domain.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.geocab.domain.entity.accessgroup.AccessGroup;
import br.com.geocab.domain.entity.accessgroup.AccessGroupCustomSearch;
import br.com.geocab.domain.entity.accessgroup.AccessGroupLayer;
import br.com.geocab.domain.entity.account.User;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.layer.CustomSearch;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.tool.Tool;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupCustomSearchRepository;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupLayerRepository;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupRepository;
import br.com.geocab.domain.repository.account.IUserRepository;
import br.com.geocab.domain.repository.tool.IToolRepository;


/**
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/12/2014
 * @version 1.0
 * @category Service
 */
@Service
@Transactional
@RemoteProxy(name="accessGroupService")
@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
public class AccessGroupService
{
	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	@Autowired
	private IAccessGroupRepository accessGroupRepository;
	
	@Autowired
	private IToolRepository toolRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IAccessGroupCustomSearchRepository accessGroupCustomSearchRepository;
	
	@Autowired
	private IAccessGroupLayerRepository accessGroupLayerRepository;
	
	@Autowired
	private LayerGroupService layerGroupService;
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * Method to insert and {@link AccessGroup}
	 * 
	 * @param accessGroup
	 * @return {@link AccessGroup}
	 */
	public AccessGroup insertAccessGroup( AccessGroup accessGroup)
	{
		return this.accessGroupRepository.save( accessGroup );
	}
	
	/**
	 * 
	 * Method to update an {@link AccessGroup}
	 * 
	 * @param accessGroup
	 * @return {@link AccessGroup}
	 */
	public AccessGroup updateAccessGroup( AccessGroup accessGroup )
	{
		return this.accessGroupRepository.save( accessGroup );
	}
	
	/**
	 * Method to remove an {@link AccessGroup}
	 * 
	 * @param id
	 */
	public void removeAccessGroup( Long id )
	{
		// public access group can't be removed
		Assert.isTrue(id != 1);
		
		this.removeAllAssociationsFromAccessGroup( id );
		this.accessGroupRepository.delete( id );
	}
	
	/**
	 * 
	 * Method to find an {@link AccessGroup} by id
	 * 
	 * @param id
	 * @return {@link AccessGroup} 
	 */
	@Transactional(readOnly = true)
	public AccessGroup findAccessGroupById( Long id )
	{
		return this.accessGroupRepository.findOne( id );
		
	}
	
	/**
	 * Method to list access groups pageable with filter option
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<AccessGroup> listAccessGroupByFilters( String filter, PageRequest pageable )
	{
		return this.accessGroupRepository.listByFilters(filter, pageable);
	}
	
	/**
	 * 
	 * @param customsSearch  (Survey List customized to be associated with the access group).
	 * @param accessGroupId (Access group to be associated).
	 */
	public void linkCustomSearch( List<CustomSearch> customsSearch, Long accessGroupId )
	{
		AccessGroup accessGroup = new AccessGroup(accessGroupId);
		for (CustomSearch customSearch : customsSearch)
		{
			AccessGroupCustomSearch accessGroupCustomSearch = new AccessGroupCustomSearch();
			accessGroupCustomSearch.setAccessGroup(accessGroup);
			accessGroupCustomSearch.setCustomSearch(customSearch);
			
			this.accessGroupCustomSearchRepository.save(accessGroupCustomSearch);
		}
	}
	
	/**
	 * 
	 * @param layers (Layer list to be associated with the access group).
	 * @param accessGroupId (Access group to be associated).
	 */
	public void linkLayer( List<Layer> layers, Long accessGroupId )
	{
		AccessGroup accessGroup = new AccessGroup(accessGroupId);
		for (Layer layer : layers)
		{
			AccessGroupLayer accessGroupLayer = new AccessGroupLayer();
			accessGroupLayer.setAccessGroup(accessGroup);
			accessGroupLayer.setLayer(layer);
			
			this.accessGroupLayerRepository.save(accessGroupLayer);
		}
	}
	
	/**
	 * 
	 * @param layers (Layer to be disfellowshipped).
	 * @param accessGroupId (Access group to be disassociated).
	 */
	public void unlinkLayer( List<Layer> layers, Long accessGroupId )
	{
		for (Layer layer : layers)
		{
			List<AccessGroupLayer> AccessGroupLayers = this.accessGroupLayerRepository.listByAccessGroupLayerId(accessGroupId, layer.getId());
			for (AccessGroupLayer accessGroupLayer : AccessGroupLayers)
			{
				this.accessGroupLayerRepository.delete(accessGroupLayer);
			}
		}
	}
	
	/**
	 * 
	 * @param customsSearch (Custom Search to be disfellowshipped).
	 * @param accessGroupId (Access group to be disassociated).
	 */
	public void unlinkCustomSearch( List<CustomSearch> customsSearch, Long accessGroupId )
	{
		for (CustomSearch customSearch : customsSearch)
		{
			List<AccessGroupCustomSearch> AccessGroupCustomSearchs = this.accessGroupCustomSearchRepository.listByAccessGroupCustomSearchId(accessGroupId, customSearch.getId());
			for (AccessGroupCustomSearch accessGroupCustomSearch : AccessGroupCustomSearchs)
			{
				this.accessGroupCustomSearchRepository.delete(accessGroupCustomSearch);
			}
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public ArrayList<Layer> listLayerByAccessGroupId(Long id)
	{
		ArrayList<Layer> layers = new ArrayList<Layer>();
		
		List<AccessGroupLayer> accessGroupLayers = this.accessGroupLayerRepository.listByAccessGroupId(id);
		
		for (AccessGroupLayer accessGroupLayer : accessGroupLayers)
		{
			if(accessGroupLayer.getLayer().getDataSource().getUrl() != null) {
				accessGroupLayer.getLayer().setLegend(layerGroupService.getLegendLayerFromGeoServer(accessGroupLayer.getLayer()));
			}
			
			layers.add(accessGroupLayer.getLayer());
		}
		
		return layers;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public ArrayList<CustomSearch> listCustomSearchByAccessGroupId(Long id)
	{
		ArrayList<CustomSearch> customsSearch = new ArrayList<CustomSearch>();
		
		List<AccessGroupCustomSearch> accessGroupCustomSearchs = this.accessGroupCustomSearchRepository.listByAccessGroupId(id);
		
		for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroupCustomSearchs)
		{
			if(accessGroupCustomSearch.getCustomSearch().getLayer().getDataSource().getUrl() != null){
				accessGroupCustomSearch.getCustomSearch().getLayer().setLegend(layerGroupService.getLegendLayerFromGeoServer(accessGroupCustomSearch.getCustomSearch().getLayer()));
			}
			
			customsSearch.add(accessGroupCustomSearch.getCustomSearch());
		}
		
		return customsSearch;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Tool> listTools()
	{
		return this.toolRepository.findAll();
	}
	
	/**
	 * 
	 * @param accessGroupId
	 * @param users
	 */
	public void updateAccessGroupUsers(Long accessGroupId, Set<User> users)
	{
		// Access Group isn't allow to insert/remove users
		Assert.isTrue(accessGroupId != 1);

		AccessGroup accessGroup = this.accessGroupRepository.findOne(accessGroupId);
		
		Set<User> usersDB = new HashSet<User>();
		for(User user : users) {
			User userDB = userRepository.findOne(user.getId());
			usersDB.add(userDB);
		}
		
		accessGroup.setUsers(usersDB);
		this.accessGroupRepository.save(accessGroup);
	}
	
	/**
	 * 
	 * @param accessGroupId
	 * @param tools
	 */
	@Transactional(readOnly=false)
	public void updateAccessGroupTools(Long accessGroupId, Set<Tool> tools)
	{
		for (Tool tool: tools) 
		{
			this.toolRepository.save(tool);
		}
		AccessGroup accessGroup = this.accessGroupRepository.findOne(accessGroupId);
		accessGroup.setTools(tools);
		this.accessGroupRepository.save(accessGroup);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @return
	 */
	public AccessGroup saveAccessGroupNameDescription(Long id, String name, String description)
	{
		AccessGroup accessGroup;
		if (id != null)
		{
			accessGroup = this.accessGroupRepository.findOne(id);
			accessGroup.setName(name);
			accessGroup.setDescription(description);
			return this.updateAccessGroup(accessGroup);
		}
		else
		{
			accessGroup = new AccessGroup();
			accessGroup.setName(name);
			accessGroup.setDescription(description);
			return this.insertAccessGroup(accessGroup);
		}
	}
	
	/**
	 * 
	 * @param accessGroupId
	 */
	private void removeAllAssociationsFromAccessGroup(Long accessGroupId)
	{
		List<AccessGroupLayer> layers = this.accessGroupLayerRepository.listByAccessGroupId(accessGroupId);
		for (AccessGroupLayer accessGroupLayer : layers)
		{
			this.accessGroupLayerRepository.delete(accessGroupLayer);
		}
		List<AccessGroupCustomSearch> accessGroupCustomSearchs = this.accessGroupCustomSearchRepository.listByAccessGroupId(accessGroupId);
		for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroupCustomSearchs)
		{
			this.accessGroupCustomSearchRepository.delete(accessGroupCustomSearch);
		}
	}
}
