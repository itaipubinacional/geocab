/**
 * 
 */
package br.com.geocab.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.ConstraintViolationException;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.accessgroup.AccessGroup;
import br.com.geocab.domain.entity.accessgroup.AccessGroupCustomSearch;
import br.com.geocab.domain.entity.account.UserRole;
import br.com.geocab.domain.entity.layer.CustomSearch;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupCustomSearchRepository;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupRepository;
import br.com.geocab.domain.repository.customsearch.ICustomSearchRepository;


/**
 * Relevant class to control the actions of {@link CustomSearch}}
 * 
 * @author Thiago Rossetto Afonso
 * @since 25/06/2014
 * @version 1.0
 * @category Service
 *
 */

@Service
@Transactional
@RemoteProxy(name="customSearchService")
@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
public class CustomSearchService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	private static final Logger LOG = Logger.getLogger( DataSourceService.class.getName() );
	/**
	 * Repository of an {@link CustomRepository}
	 */
	@Autowired
	private ICustomSearchRepository customSearchRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IAccessGroupCustomSearchRepository accessGroupCustomSearchRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IAccessGroupRepository accessGroupRepository;
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * Method to insert an {@link CustomSearch}
	 * 
	 * @param MarkerModeration
	 * @return CustomSearch
	 */
	public Long insertCustomSearch( CustomSearch customSearch )
	{
		//Assert.notNull(customSearch.getLayer(), Messages.getException( "pesquisapersonalizada.camada" ));
		try{
			customSearch = this.customSearchRepository.save( customSearch );	
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			//throw new IllegalArgumentException( Messages.getException( "pesquisapersonalizada.nome_existe" ) );
		}
		for (AccessGroupCustomSearch accessGroupCustomSearch : customSearch.getAccessGroupCustomSearch())
		{
			accessGroupCustomSearch.setCustomSearch(customSearch);
			this.accessGroupCustomSearchRepository.save(accessGroupCustomSearch);
		}
		
		return customSearch.getId();
	}
	
	/**
	 * Method to update an {@link CustomSearch}
	 * 
	 * @param customSearch
	 * @return
	 */
	public CustomSearch updateCustomSearch( CustomSearch customSearch )
	{
		//Assert.notNull(customSearch.getLayer(), Messages.getException( "pesquisapersonalizada.camada" ));
		try{
			/* 
			 * On update isn't allow to modify an layer
			 * The original layer stay. 
			 * 
			 * */
			CustomSearch customSearchDatabase = this.findCustomSearchById(customSearch.getId());
			customSearch.setLayer(customSearchDatabase.getLayer());
			
			customSearch = this.customSearchRepository.save( customSearch );
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
			//throw new IllegalArgumentException( Messages.getException( "pesquisapersonalizada.nome_existe" ) );
		}
		catch ( ConstraintViolationException e ){
			LOG.info( e.getMessage() );
			//throw new IllegalArgumentException( Messages.getException( "pesquisapersonalizada.nome_existe" ) );
		}
		
		return customSearch;
	}
	
	/**
	 * Method to remove an {@link customSearch}
	 * 
	 * @param id
	 */
	public void removeCustomSearch( Long id )
	{
		List<AccessGroupCustomSearch> accessGroupCustomSearchs = this.accessGroupCustomSearchRepository.listByCustomSearchId( id );
		for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroupCustomSearchs)
		{
			this.accessGroupCustomSearchRepository.delete(accessGroupCustomSearch);
		}
		
		this.customSearchRepository.delete( id );
	}
	
	/**
	 * Method to find an {@link CustomSearch} by id
	 * 
	 * @param id
	 * @return customSearch
	 */
	@Transactional(readOnly = true)
	public CustomSearch findCustomSearchById( Long id )
	{
		CustomSearch customSearch = this.customSearchRepository.findOne( id );
		
		// Get the legend of GeoServer's layer
		if (customSearch.getLayer().getDataSource().getUrl() != null){
			int position = customSearch.getLayer().getDataSource().getUrl().lastIndexOf("ows?");
			String urlGeoserver = customSearch.getLayer().getDataSource().getUrl().substring(0, position);
			String urlLegend = urlGeoserver + Layer.LEGEND_GRAPHIC_URL + customSearch.getLayer().getName() + Layer.LEGEND_GRAPHIC_FORMAT;
			customSearch.getLayer().setLegend(urlLegend);
		}
		
		return customSearch;
	}
	
	/**
	 * Method to list custom search pageable with filter option
	 *
	 * @param filter
	 * @param pageable
	 * @return customSearch
	 */
	@Transactional(readOnly=true)
	public Page<CustomSearch> listCustomSearchByFilters( String filter, PageRequest pageable )
	{
		Page<CustomSearch> customsSearch =  this.customSearchRepository.listByFilters(filter, pageable);
		
		for ( CustomSearch customSearch : customsSearch.getContent() )
		{
			if(customSearch.getLayer().getDataSource().getUrl() != null) {
				int position = customSearch.getLayer().getDataSource().getUrl().lastIndexOf("ows?");
				String urlGeoserver = customSearch.getLayer().getDataSource().getUrl().substring(0, position);
				String urlLegend = urlGeoserver + Layer.LEGEND_GRAPHIC_URL + customSearch.getLayer().getName() + Layer.LEGEND_GRAPHIC_FORMAT;
				customSearch.getLayer().setLegend(urlLegend);
			}
		}
		
		return customsSearch;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public ArrayList<AccessGroup> listAccessGroupBySearchId(Long id)
	{
		ArrayList<AccessGroup> accessGroups = new ArrayList<AccessGroup>();
		
		List<AccessGroupCustomSearch> accessGroupCustomSearchs = this.accessGroupCustomSearchRepository.listByCustomSearchId(id);
		
		for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroupCustomSearchs)
		{
			accessGroups.add(accessGroupCustomSearch.getAccessGroup());
		}
		
		return accessGroups;
	}
	
	/**
	 * Method that return an list of custom searchs according the access group of user
	 */
	@PreAuthorize("true")
	public List<CustomSearch> listCustomSearchsByUser()
	{
		List<CustomSearch> customsSearchUser = new ArrayList<CustomSearch>();
		
		List<AccessGroup> accessGroupUser = null;
		//List of all access groups of user
		if(ContextHolder.getAuthenticatedUser() != null) 
		{
			accessGroupUser = this.accessGroupRepository.listByUser(ContextHolder.getAuthenticatedUser().getEmail());
		} 
		else 
		{
			accessGroupUser = this.accessGroupRepository.listPublicGroups();
		}
		
		for (AccessGroup accessGroup : accessGroupUser)
		{
			accessGroup = this.accessGroupRepository.findOne( accessGroup.getId() );
			
			for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroup.getAccessGroupCustomSearch())
			{	
				if( !customsSearchUser.contains(accessGroupCustomSearch.getCustomSearch()) )
				{
					customsSearchUser.add(accessGroupCustomSearch.getCustomSearch());
				}
			}
		}
		
		
		return customsSearchUser;
	}
	
	/**
	 * 
	 * @param customSearch (Survey List customized to be associated with the access group).
	 * @param AccessGroup (Access group to be associated).
	 */
	public void linkAccessGroup( List<AccessGroup> accessGroups, Long customSearchId )
	{
		CustomSearch customSearch = new CustomSearch(customSearchId);
		for (AccessGroup accessGroup : accessGroups)
		{
			AccessGroupCustomSearch accessGroupCustomSearch = new AccessGroupCustomSearch();
			accessGroupCustomSearch.setAccessGroup(accessGroup);
			accessGroupCustomSearch.setCustomSearch(customSearch);
			
			this.accessGroupCustomSearchRepository.save(accessGroupCustomSearch);
		}
	}
	
	/**
	 * 
	 * @param accessGroup
	 * @param customSearch
	 */
	public void unlinkAccessGroup( List<AccessGroup> accessGroups, Long customSearchId )
	{
		for (AccessGroup accessGroup : accessGroups)
		{
			List<AccessGroupCustomSearch> accessGroupCustomSearchs = this.accessGroupCustomSearchRepository.listByAccessGroupCustomSearchId(accessGroup.getId(), customSearchId);
			for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroupCustomSearchs)
			{
				this.accessGroupCustomSearchRepository.delete(accessGroupCustomSearch);
			}
		}
	}
}
