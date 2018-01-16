/**
 * 
 */
package br.com.geocab.domain.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletContext;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.JAXBException;

import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.json.parse.JsonParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.geocab.application.security.ContextHolder;
import br.com.geocab.domain.entity.accessgroup.AccessGroup;
import br.com.geocab.domain.entity.accessgroup.AccessGroupLayer;
import br.com.geocab.domain.entity.configuration.account.User;
import br.com.geocab.domain.entity.configuration.account.UserRole;
import br.com.geocab.domain.entity.datasource.DataSource;
import br.com.geocab.domain.entity.layer.Attribute;
import br.com.geocab.domain.entity.layer.AttributeOption;
import br.com.geocab.domain.entity.layer.AttributeType;
import br.com.geocab.domain.entity.layer.ExternalLayer;
import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.domain.entity.layer.LayerField;
import br.com.geocab.domain.entity.layer.LayerFieldType;
import br.com.geocab.domain.entity.layer.LayerGroup;
import br.com.geocab.domain.entity.tool.Tool;
import br.com.geocab.domain.repository.IFileRepository;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupLayerRepository;
import br.com.geocab.domain.repository.accessgroup.IAccessGroupRepository;
import br.com.geocab.domain.repository.attribute.IAttributeRepository;
import br.com.geocab.domain.repository.layergroup.IAttributeOptionRepository;
import br.com.geocab.domain.repository.layergroup.ILayerGroupRepository;
import br.com.geocab.domain.repository.layergroup.ILayerRepository;
import br.com.geocab.domain.repository.tool.IToolRepository;
import br.com.geocab.infrastructure.geoserver.GeoserverConnection;


/**
 * 
 * @author Vinicius Ramos Kawamoto
 * @since 22/09/2014
 * @version 1.0
 * @category Service
 *
 */
@Service
@Transactional
@RemoteProxy(name="layerGroupService")
public class LayerGroupService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 */
	@Autowired
	private ILayerGroupRepository layerGroupRepository;
	
	/**
	 * 
	 */
	@Autowired(required=false)
	private ServletContext servletContext;
	
	/**
	 * 
	 */
	@Autowired
	private ILayerRepository layerRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IAccessGroupLayerRepository accessGroupLayerRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IAccessGroupRepository accessGroupRepository;

	/**
	 * 
	 */
	@Autowired
	private IAttributeRepository attributeRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IToolRepository toolRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IAttributeOptionRepository attributeOptionRepository;
		
	/**
	 * 
	 */
	protected RestTemplate template;
	
	//files
	/**
	 * 
	 */
	@Autowired
	private IFileRepository fileRepository;
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 * @return
	 */
	public List<String> listLayersIcons()
	{
		final String path = this.servletContext.getRealPath("/static/icons");
		
		File[] files = this.fileRepository.listFilePath(path);

		List<String> layers = new ArrayList<String>();
		
		for(File file : files) {
			if(file.isFile()){
				layers.add(file.getName());
			}
		}
		
		return layers;
	}
	
	/**
	 * Method to insert an {@link LayerGroup}
	 * 
	 * @param layerGroup
	 * @return layerGroup
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public LayerGroup insertLayerGroup( LayerGroup layerGroup )
	{
		layerGroup.setPublished(false);
		return hasChildren(this.layerGroupRepository.save( layerGroup ));		
	}
	
	/**
	 * Method to update an {@link LayerGroup}
	 * 
	 * @param layerGroup
	 * @return layerGroup
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public LayerGroup updateLayerGroup( LayerGroup layerGroup )
	{
		return hasChildren(this.layerGroupRepository.save( layerGroup ));		
	}
	
	
	
	/**
	 * Find {@link LayerGroup} by id
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public LayerGroup findLayerGroupById( Long id )
	{
		return hasChildren(this.layerGroupRepository.findOne( id ));
	}
	
	/**
	 * 
	 * @param List<layerGroup>
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public void saveAllParentLayerGroup(List<LayerGroup> layerGroup)
	{
		List<LayerGroup> layersGroups = this.layerGroupRepository.listAllParentLayerGroup();
		
		for (int i = 0; i < layersGroups.size(); i++)
		{
			layersGroups.get(i).setOrderLayerGroup(i);
			this.layerGroupRepository.save( layersGroups.get(i) );
		}
	}
	
	
	
	/**
	 * M�todo que seta todos os grupos publicados filhos em seus respectivos grupos publicados pai
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	private void populateChildrenInLayerGroupPublished()
	{
		final List<LayerGroup> layersGroupPublished = this.layerGroupRepository.listAllLayersGroupPublished();
		
		for (LayerGroup layerGroupPublished : layersGroupPublished)
		{
			//Pega o grupo de camadas pois a query anterior (layerGroupRepository.listAllLayersGroupPublished()) foi alterada e n�o pega todos os atributos da camada
			layerGroupPublished = this.layerGroupRepository.findOne(layerGroupPublished.getId());
			
			layerGroupPublished.setLayersGroup(this.layerGroupRepository.listLayersGroupPublishedChildren(layerGroupPublished.getId()));
		
			this.layerGroupRepository.save(layerGroupPublished);	
		}
	}
	
	
	/**
	 * M�todo recursivo que remove os grupos de camadas publicados filhos
	 * @param gruposCamadasPublicados
	 * @param grupoCamadaPublicadosSuperior
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	private void removeLayerGroupPublished( LayerGroup layerGroupPublished )
	{
		if ( layerGroupPublished.getLayersGroup() != null )
		{
			for (LayerGroup layerGroupChildPublished : layerGroupPublished.getLayersGroup())
			{
				// remove quando o rascunho for null e o publicado is true
				if (layerGroupChildPublished.getPublished() && layerGroupChildPublished.getDraft() == null )
				{
					removeLayerGroupPublished(layerGroupChildPublished);
					this.layerGroupRepository.delete(layerGroupChildPublished);
				}	
			}	
		}
		
	}
	
	
	/**
	 * 
	 * @param grupoCamadaOriginal
	 * @param grupoCamadaPaiPublicado
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public void recursive( LayerGroup layerGroupOriginal, LayerGroup layerGroupUpperPublished )
	{
		final Long layerGroupOriginalId = layerGroupOriginal.getId();
		
		// verifica se foi possui o grupo publicado
		final LayerGroup layerGroupPublishedExistent = this.layerGroupRepository.findByDraftId(layerGroupOriginalId);
		
		// efetua a c�pia do grupo de camadas original
		LayerGroup layerGroupPublished = new LayerGroup();
		BeanUtils.copyProperties(layerGroupOriginal, layerGroupPublished);
		
		// update nos dados do grupo publicado
		layerGroupPublished.setPublished(true);
		layerGroupPublished.setLayerGroupUpper(layerGroupUpperPublished);
		layerGroupPublished.setDraft(new LayerGroup(layerGroupOriginalId));
		layerGroupPublished.setLayersGroup(new ArrayList<LayerGroup>());
		layerGroupPublished.setLayers(new ArrayList<Layer>());
		
		// se j� possui o grupo criado apenas altera o existente sentido cria o grupo publicado
		if (layerGroupPublishedExistent != null)
		{
			layerGroupPublished.setId(layerGroupPublishedExistent.getId());
			layerGroupPublished = this.layerGroupRepository.save(layerGroupPublished);
		} 
		else
		{
			layerGroupPublished.setId(null);
			layerGroupPublished = this.layerGroupRepository.save(layerGroupPublished);
		}
		
		// cria��o atualiza��o de camadas para camadas publicadas
		if ( layerGroupOriginal.getLayers() != null )
		{
			for ( Layer layerOriginal : layerGroupOriginal.getLayers() )
			{
				//final Camada camadaPublicadaExistente = this.camadaRepository.findByRascunhoId(camadaOriginal.getId());
				
				// cria��o da camada publicada que iria conter a ordem publicada e os grupos
				Layer layerPublished = new Layer();
//				BeanUtils.copyProperties(camadaOriginal, camadaPublicada);
				
				
				// cria��o/update na camada publicada
				layerPublished.setName(layerOriginal.getName());
				layerPublished.setTitle(layerOriginal.getTitle());
				layerPublished.setIcon(layerOriginal.getIcon());
				layerPublished.setDataSource(new DataSource(layerOriginal.getDataSource().getId()));
				layerPublished.setMinimumScaleMap(layerOriginal.getMinimumScaleMap());
				layerPublished.setMaximumScaleMap(layerOriginal.getMaximumScaleMap());
				layerPublished.setOrderLayer(layerOriginal.getOrderLayer());
				layerPublished.setLayerGroup(layerGroupPublished);
				layerPublished.setPublished(true);
				
				// se j� possui a camada publicada apenas altera a existente sentido cria a camada publicada
				if (layerOriginal.getPublishedLayer() != null)
				{
					layerPublished.setId(layerOriginal.getPublishedLayer().getId());
				} 
				else
				{
					layerPublished.setId(null);
				}
				
				layerPublished = this.layerRepository.save(layerPublished);
				
				layerGroupPublished.getLayers().add(layerPublished);
				layerGroupPublished = this.layerGroupRepository.save(layerGroupPublished);
				
				// update na camada original
				layerOriginal.setPublishedLayer(layerPublished);
				layerOriginal = this.layerRepository.save(layerOriginal);
			}
		}
		
		// faz a recursivo para atualizar todos os filhos
		if ( layerGroupPublished.getLayersGroup() != null)
		{
			if ( layerGroupOriginal.getLayersGroup() != null )
			{
				for ( LayerGroup layerGroupOriginalChild : layerGroupOriginal.getLayersGroup() )
				{
					this.recursive( layerGroupOriginalChild, layerGroupPublished );
				}
			}
		}					
	}
	
	/**
	 * 
	 * @param List<layerGroup>
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public void publishLayerGroup(List<LayerGroup> layersGroup)
	{
		for (LayerGroup layerGroup : layersGroup) {
			for (Layer layer : layerGroup.getLayers()) {
				layer = this.layerRepository.findOne(layer.getId());
			}
		}
		this.saveAllLayersGroup(layersGroup); // save layersGroup
		
		final List<LayerGroup> layerGroupOriginals = this.listLayersGroupUpper();//list parent groups
		
		for ( LayerGroup layerGroupOriginal : layerGroupOriginals )
		{
			this.recursive(layerGroupOriginal, null);//recursion to insert or update a layers group published	
		}

		//exclude published groups
		//set children groups in correct parent group
		this.populateChildrenInLayerGroupPublished();
		
		final List<LayerGroup> layersGroupPublished = this.layerGroupRepository.listLayersGroupUpperPublished();
		
		
		if ( layersGroupPublished != null )
		{
			for ( LayerGroup layerGroupPublished : layersGroupPublished )
			{
				this.removeLayerGroupPublished(layerGroupPublished);
				
				// remove o grupo de camada publicado superior
				// remove quando o rascunho for null e o publicado is true
				if (layerGroupPublished.getPublished() && layerGroupPublished.getDraft() == null)
				{
					this.layerGroupRepository.delete(layerGroupPublished);
				}
			}
		}
	}
	
	/**
	 * Method to save a list of {@link LayerGroup}
	 * 
	 * @param List<layerGroup>
	 * @return
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public void saveAllLayersGroup( List<LayerGroup> layerGroup )
	{		
		this.prioritizeLayersGroup( layerGroup, null );	
		
		this.prioritizeLayers( layerGroup);
	}
	
	/**
	 * 
	 * @param layerGroups
	 * @param layerGroupUpper
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	private void prioritizeLayersGroup( List<LayerGroup> layerGroups, LayerGroup layerGroupUpper )
	{
		if ( layerGroups != null )
		{
			for (int i = 0; i < layerGroups.size(); i++)
			{
				layerGroups.get(i).setOrderLayerGroup(i);
				layerGroups.get(i).setLayerGroupUpper(layerGroupUpper);
			
				this.layerGroupRepository.save( layerGroups.get(i) );	
				prioritizeLayersGroup(layerGroups.get(i).getLayersGroup(), layerGroups.get(i));		
			}
		}
	}
	
	/**
	 * 
	 * @param layerGroups
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	private void prioritizeLayers( List<LayerGroup> layerGroups )
	{		
		for (LayerGroup layerGroup : layerGroups)
		{
			if (layerGroup.getLayers() != null)
			{
				for(int j = 0; j < layerGroup.getLayers().size(); j++)
				{
					Layer layerToUpdate = layerGroup.getLayers().get(j);
					layerToUpdate.setOrderLayer(j);
					layerToUpdate.setLayerGroup(layerGroup);
					
					this.layerRepository.save( layerToUpdate );
				}
				if (layerGroup.getLayersGroup() != null) prioritizeLayers(layerGroup.getLayersGroup());
			}			
		}	
	}
	
	/**
	 * M�todo para remover um {@link GrupoCamadas}
	 * 
	 * @param id
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public void removeLayerGroup( Long id )
	{
		final LayerGroup layerGroup = this.layerGroupRepository.findOne(id);
		
		LayerGroup layerGroupPublished = this.layerGroupRepository.findByDraftId(id);
		
		// verifica se existe o grupo j� publicado
		if (layerGroupPublished != null)
		{
			// seta null no campo rascunho do grupo de camada publicado para permitir excluir o grupo original
			layerGroupPublished.setDraft(null);
			layerGroupPublished = this.layerGroupRepository.save(layerGroupPublished);
		}
		
		this.layerGroupRepository.delete( layerGroup );
	}
	
	/**
	 * 
	 * @param filter
	 * @param idExcluso
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<LayerGroup> listLayersGroupUpper()
	{
		List<LayerGroup> layersGroup = this.layerGroupRepository.listLayersGroupUpper();
				
		List<LayerGroup> listLayersGroups = hasChildren(layersGroup);
		
		for (LayerGroup layerGroup : listLayersGroups)
		{
			for (Layer layer : layerGroup.getLayers())
			{
				this.setIcon(layer);
			}
		}
		return listLayersGroups;
	}
	
	/**
	 * Lista os Grupos de camadas publicados
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public LayerGroup listLayersGroupPublishedByLayerGroupId(Long id)
	{
		return hasChildren(this.listLayersGroupByLayerGroupId(id , true));
	}
	
	/**
	 * Lista os Grupos de camadas n�o publicados
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public LayerGroup listLayersGroupByLayerGroupId(Long id)
	{		
		return hasChildren(this.listLayersGroupByLayerGroupId(id , false));
	}
	
	/**
	 * Lista os Grupos de camadas
	 * 
	 * @param id
	 * @param published
	 * @return
	 */
	@Transactional(readOnly=true)
	public LayerGroup listLayersGroupByLayerGroupId(Long id, Boolean published)
	{
		
		LayerGroup layerGroup = this.layerGroupRepository.findLayerGroupById( id );			
	
		List<LayerGroup> layersGroup = this.layerGroupRepository.listLayersGroupByLayerGroupId( id, published);
		
		//Se o grupo de camadas est� vazio (se n�o tem outros grupos de camadas internamente) pega as camadas desse grupo de camadas
		if( layersGroup.isEmpty() )
		{
			
			List<Layer> layers = this.layerRepository.listLayersByLayerGroupId( id, published );
			
			layerGroup.setLayers(layers);
			
		}
		//Se o grupo de camadas tem outros grupos de camadas internamente, seta o grupo de camadas
		else
		{
			layerGroup.setLayersGroup(layersGroup);
		} 
		return hasChildren(layerGroup);	
	}
	
	
	/**
	 * M�todo que retorna a estrutura completa dos grupos de camadas publicados
	 * @param filter
	 * @param idExcluso
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<LayerGroup> listLayerGroupUpperPublished()
	{
		List<LayerGroup> layersGroupUpperPublished = new ArrayList<LayerGroup>();
		final List<LayerGroup> layersGroupPublished = this.layerGroupRepository.listAllLayersGroupPublished();
		
		if ( layersGroupPublished != null )
		{
			for (LayerGroup layerGroupPublished : layersGroupPublished)
			{
				layerGroupPublished.setLayers(this.layerRepository.listLayersByLayerGroupPublished(layerGroupPublished.getId()));
				
				if (layerGroupPublished.getLayerGroupUpper() == null || layerGroupPublished.getLayerGroupUpper().getId() == null)
				{
					layersGroupUpperPublished.add(layerGroupPublished);
				}				
			}
		}
		
		
		for (LayerGroup layerGroup : layersGroupPublished)
		{
			for (Layer layer : layerGroup.getLayers())
			{
				layer = this.setIcon(layer);
			}
		}
		//Se o usu�rio for administrador, ele poder� visualizar todas os grupos de acesso.
	
		return this.layersGroupUpperByRole(layersGroupUpperPublished);
		
	}
	
	
	/**
	 * 
	 * @param layersGroupUpperPublished
	 * @return
	 */
	private List<LayerGroup> layersGroupUpperByRole( List<LayerGroup> layersGroupUpperPublished )
	{
		
		List<AccessGroup> accessGroupsUser =  new ArrayList<AccessGroup>();
		final User user = ContextHolder.getAuthenticatedUser();
		
		if (!user.equals(User.ANONYMOUS))
		{
			if( user.getRole() != UserRole.ADMINISTRATOR ) 
			{
				accessGroupsUser = this.accessGroupRepository.listByUser(user.getEmail());
				
				for (AccessGroup accessGroup : accessGroupsUser)
				{
					accessGroup.setAccessGroupLayer(new HashSet<AccessGroupLayer>(this.accessGroupLayerRepository.listByAccessGroupId(accessGroup.getId())) );
				}
				
				if ( !layersGroupUpperPublished.isEmpty() )
				{
					verifyLayerPermission(layersGroupUpperPublished, accessGroupsUser);
				}
				
				List<LayerGroup> layerGroupToDelete = new ArrayList<LayerGroup>();
				
				for ( LayerGroup layerGroup : layersGroupUpperPublished )
				{
					this.removeLayerGroupEmptyPublished(layerGroup);
					
					if (layerGroup.getLayersGroup().isEmpty() & layerGroup.getLayers().isEmpty())
					{
						layerGroupToDelete.add(layerGroup);
					}
				}
				layersGroupUpperPublished.removeAll(layerGroupToDelete);
			}
		}
		else 
		{
			AccessGroup accessGroup = this.accessGroupRepository.findById(AccessGroup.PUBLIC_GROUP_ID);
			accessGroupsUser.add(accessGroup);
			
			accessGroup.setAccessGroupLayer(new HashSet<AccessGroupLayer>(this.accessGroupLayerRepository.listByAccessGroupId(accessGroup.getId())) );
			
			if ( !layersGroupUpperPublished.isEmpty() )
			{
				verifyLayerPermission(layersGroupUpperPublished, accessGroupsUser);
			}
			
			List<LayerGroup> layerGroupToDelete = new ArrayList<LayerGroup>();
			
			for ( LayerGroup layerGroup : layersGroupUpperPublished )
			{
				this.removeLayerGroupEmptyPublished(layerGroup);
				
				if (layerGroup.getLayersGroup().isEmpty() & layerGroup.getLayers().isEmpty())
				{
					layerGroupToDelete.add(layerGroup);
				}
			}
			layersGroupUpperPublished.removeAll(layerGroupToDelete);
		}
		
		return layersGroupUpperPublished;
		
	}
	
	/**
	 * 
	 * @param gruposCamadas
	 * @param gruposAcessosUsuario
	 */
	private void verifyLayerPermission( List<LayerGroup> layerGroups, List<AccessGroup> accessGroupUser )
	{
		boolean hasAccess = false;
		
		if ( layerGroups != null )
		{ 
			if ( !layerGroups.isEmpty() )
			{
				for (LayerGroup group: layerGroups)
				{
					if( group.getLayers() != null )
					{					
						if( group.getLayers().size() > 0 )
						{
							List<Layer> layersToDelete = new ArrayList<Layer>();
							
							for(Layer layer : group.getLayers())
							{
								if ( !accessGroupUser.isEmpty() )
								{
									for (AccessGroup accessGroup: accessGroupUser)
									{
										if ( !accessGroup.getAccessGroupLayer().isEmpty() )
										{
											for (AccessGroupLayer accessGroupLayer : accessGroup.getAccessGroupLayer())
											{
												if(layer.getId().equals(accessGroupLayer.getLayer().getId()))
												{
													hasAccess = true;
													break;
												}
												else
												{
													hasAccess = false;
												}
											}
										}
										
										if (hasAccess)
										{
											break;
										}
									}
								}
								
								if( !hasAccess )
								{
									layersToDelete.add(layer);
								} else {
									hasAccess = false;
								}
							}
							group.getLayers().removeAll(layersToDelete);
						}
					}
					
					verifyLayerPermission(group.getLayersGroup(), accessGroupUser);
				}
				
			}
		}
	}
	
	/**
	 * 
	 * @param grupoCamadas
	 */
	private void removeLayerGroupEmptyPublished( LayerGroup layerGroups )
	{
		if ( layerGroups.getLayersGroup() != null )
		{
			List<LayerGroup> layerGroupToDelete = new ArrayList<LayerGroup>();
			
			for (LayerGroup layerGroupChildren: layerGroups.getLayersGroup())
			{
				
				removeLayerGroupEmptyPublished(layerGroupChildren);
				
				// remove o grupo de camada publicado superior vazio
				if (layerGroupChildren.getLayersGroup().isEmpty() & layerGroupChildren.getLayers().isEmpty())
				{
					layerGroupToDelete.add(layerGroupChildren);
				}
				
			}
			
			layerGroups.getLayersGroup().removeAll(layerGroupToDelete);
		}
		
	}

	/**
	 * Traz a legenda da camada do GeoServer
	 * @param layer
	 * @return
	 */
	private Layer setIcon(Layer layer)
	{
		if( layer.getDataSource() != null && layer.getDataSource().getUrl() != null )
		{
			layer.setLegend((getLegendLayerFromGeoServer(layer)));	
			layer.setIcon((layer.getLegend()));
		}
		return layer;
	}
	
	
	/**
	 * 
	 * @param layer
	 * @param sUrl
	 * @return
	 */
	private String getUrl(Layer layer, String sUrl)
	{
		if(layer.getDataSource().getToken()!= null)
		{
			sUrl += "&authkey=" + layer.getDataSource().getToken();	
		}	
		return sUrl;
	}
	
	/**
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<LayerGroup> listLayerGroups(String filter, PageRequest pageable)
	{
		return this.hasChildren(this.layerGroupRepository.listByFilter(filter, pageable));
	}
	
	

	/**
	 * N�o � necess�ria a sincroniza��o com hasChildren 
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<LayerGroup> listAllLayerGroups()
	{		
		return this.layerGroupRepository.findAll();
	}
	
	/**
	 * 
	 * @return camadas filtradas pelos grupos de acesso do usu�rio
	 */
	@Transactional(readOnly=true)
	public List<Layer> listAllInternalLayerGroups()
	{
		User user = ContextHolder.getAuthenticatedUser();
		if (user.getRole().equals(UserRole.ADMINISTRATOR))
		{
			return this.layerRepository.listAllInternalLayerGroups();	
		}
		return this.layerRepository.listAllInternalLayerGroupsAndByUser(user.getId());
	}
	
	/**
	 * 
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Layer> listAllInternalLayers()
	{
		return this.layerRepository.listAllInternalLayers();
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws JAXBException
	 */
	@Transactional(readOnly=true)
	public List<ExternalLayer> listExternalLayersByFilters( DataSource dataSource )
	{
		GeoserverConnection geoserverConnection = new GeoserverConnection();
		return geoserverConnection.listExternalLayersByFilters(dataSource);
		
	}
	
	 /**
	  * 
	  * @param layer
	  * @param dataSource
	  * @return
	  */
    @Transactional(readOnly=true)
    public List<LayerGroup> listSupervisorsFilter(String layer, Long dataSource)
    {    	
    	List<LayerGroup> layersGroup = this.layerGroupRepository.listSupervisorsFilter(layer, dataSource);
                 
        return hasChildren(layersGroup);
         
    }
	
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws JAXBException
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Transactional(readOnly=true)
	public List<LayerField> listFieldLayersByFilter(Layer layer)
	{
		if(layer.getDataSource().getUrl() == null){
			List<LayerField> layerFields = new ArrayList<LayerField>();
			List<Attribute>  attrs = this.listAttributesByLayer(layer.getId());
			
			for(Attribute attr : attrs) {
				LayerField layerField = new LayerField();
				layerField.setName(attr.getName());
				layerField.setAttributeId(attr.getId());
				
				if(attr.getType() == AttributeType.TEXT){
					layerField.setType(LayerFieldType.STRING);
				} else if(attr.getType() == AttributeType.DATE){
					layerField.setType(LayerFieldType.DATE);
				} else if(attr.getType() == AttributeType.NUMBER){
					layerField.setType(LayerFieldType.INT);
				} else if(attr.getType() == AttributeType.BOOLEAN){
					layerField.setType(LayerFieldType.BOOLEAN);
				}
				
				layerFields.add(layerField);
				
			}
			
			return layerFields;
		}
		
		int position = layer.getDataSource().getUrl().lastIndexOf("ows?");
		String urlGeoserver = layer.getDataSource().getUrl().substring(0, position);				
		
		String sUrl = this.getUrl(layer,urlGeoserver + ExternalLayer.CAMPO_CAMADA_URL + layer.getName() );
		
		BufferedReader reader = null;
	    try 
	    {
	        URL url = new URL(sUrl);
	        reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	        {
	        	buffer.append(chars, 0, read); 
	        }
	        
			try
			{
				JSONObject json = new JSONObject(buffer.toString());
				JSONArray featureTypes = json.getJSONArray("featureTypes");
				JSONObject properties = (JSONObject) featureTypes.get(0);
				JSONArray propertiesArray = (JSONArray) properties.get("properties");
				
				List<LayerField> campos = new ArrayList<LayerField>();
				
				for (int i = 0; i < propertiesArray.length(); i++) 
				{
					
					JSONObject jsonOb = (JSONObject) propertiesArray.get(i);
							
					LayerField layerField = new LayerField();
					
					if (layerField.isValidTipoGeoserver(jsonOb.get("type").toString())){
						layerField.setName(jsonOb.get("name").toString());
						layerField.setTipoGeoServer(jsonOb.get("type").toString());
						campos.add(layerField);
					}

					//campos.add(layerField);
				}
				
				return campos;
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
	    }
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} 
	    finally 
	    {
	        if (reader != null) 
	        {
	        	 try
	 			{
	 				reader.close();
	 			}
	 			catch (IOException e)
	 			{
	 				e.printStackTrace();
	 			}
	        }
	    }
		return null;
	}

	
	/**
	 * M�todo respons�vel para listar as camadas
	 *
	 * @param filter
	 * @param idExcluso
	 * @param pageable
	 * @return camadas
	 */
//	@Transactional(readOnly=true)
	public Page<Layer> listLayersByFilters( String filter, Long dataSourceId,PageRequest pageable )
	{
		Page<Layer> layers = null; 
		
		User user = ContextHolder.getAuthenticatedUser();
	
		if (user.getRole().equals(UserRole.ADMINISTRATOR)) 
		{
			layers = this.layerRepository.listByFilters(filter, dataSourceId, pageable); //TODO porque o dataSourceId esta entrnando null?
		} else 
		{
			layers = this.layerRepository.listByFiltersAndByUser(filter, dataSourceId, user.getId(), pageable); //TODO porque o dataSourceId esta entrnando null?
		}
		
		
		for ( Layer layer : layers.getContent() )
		{
			// traz a legenda da camada do GeoServer
			if(layer.getDataSource() != null && layer.getDataSource().getUrl() != null ) {
				layer.setLegend(getLegendLayerFromGeoServer(layer));	
			}
		}
				
		return layers;
				
	}
	/**
	 * 
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<Layer> listLayersByFilters( String filter, PageRequest pageable )
	{	
		Page<Layer> layers = null; 
		
		User user = ContextHolder.getAuthenticatedUser();
		
		if (user.getRole().equals(UserRole.ADMINISTRATOR)) 
		{
			layers = this.layerRepository.listByFilters(filter, null, pageable); //TODO porque o dataSourceId esta entrnando null?
		} else 
		{
			layers = this.layerRepository.listByFiltersAndByUser(filter, null, user.getId(), pageable); //TODO porque o dataSourceId esta entrnando null?
		}

		for ( Layer layer : layers.getContent() )
		{
			// traz a legenda da camada do GeoServer
			if(layer.getDataSource() != null && layer.getDataSource().getUrl() != null ) {
				layer.setLegend(getLegendLayerFromGeoServer(layer));	
			}
		}
			
		return layers;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Layer> listLayersPublished()
	{
		return this.layerRepository.listLayersPublished();
	}
	
	/**
	 * Method to inserir uma {@link Layer}
	 * @param layer
	 * @return
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public Layer insertLayer( Layer layer )
	{
		layer.setLayerGroup(this.layerGroupRepository.findOne(layer.getLayerGroup().getId()));
		layer.setPublished(false);
		layer.setEnabled(layer.getEnabled() == null ? false : layer.getEnabled());
		//Valida se os atributos s�o v�lidos
		layer.validate();
		
		List<Attribute> attributes = layer.getAttributes();
		layer = this.layerRepository.save( layer );

		//Salvando na mao os atributos 
		for (Attribute attribute : attributes) 
		{
			attribute.setLayer(layer);
			List<AttributeOption> attributeOptions = attribute.getOptions();
			attribute = this.attributeRepository.save(attribute);
			for (AttributeOption attributeOption : attributeOptions) {
				attributeOption.setAttribute(attribute);
				this.attributeOptionRepository.save(attributeOption);
			}
		}
		
		layer.setAttributes(attributes);

		return layer;
	}
	/**
	 * m�todo para atualizar uma {@link Camada}
	 * 
	 * @param camada
	 * @return camada
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public Layer updateLayer( Layer layer )
	{
		layer.setLayerGroup(layer.getLayerGroup());
						
		/* Na atualiza��o n�o foi permitido modificar a fonte de dados, camada e t�tulo, dessa forma, 
		Os valores originais s�o mantidos. */
		Layer layerDatabase = this.findLayerById(layer.getId());
		layer.setDataSource(layerDatabase.getDataSource());
		layer.setName(layerDatabase.getName());
		layer.setEnabled(layer.getEnabled() == null ? false : layer.getEnabled());
		
		if(layer.getPublishedLayer() != null)
		{
			layer.setPublishedLayer(layerRepository.findById(layer.getPublishedLayer().getId()));	
		}
		
		//Handling attributes
		//Salvamos os attributos que veio com o layer a ser atualizado e logo apos pegamos os que est�o salvos no banco
		//Devemos comparar e verificar quais atributos estao salvos e nao voltaram para apagarmos e adicionarmos os novos,
		//Devemos fazer o mesmo com os options do attribute caso o attribute seja do tipo de multiple choice
		List<Attribute> attributesToUpdate = layer.getAttributes();
		List<Attribute> currentSavedAttributes = this.attributeRepository.listAttributeByLayer( layer.getId() );
		for (Attribute attribute : currentSavedAttributes) 
		{
			if ( attribute.getType().equals(AttributeType.MULTIPLE_CHOICE ) )
			{
				attribute.setOptions( this.attributeOptionRepository.listByAttributeId( attribute.getId() ) );
			}
		}

		//Vamos verificar quais attributos temos que apagar, ou seja, que est� no currentAttributes mas n�o veio no attributesToUpdate
		
		for ( Attribute currentAttribute : currentSavedAttributes ) 
		{
			boolean found = false;
			for ( Attribute attributeToUpdate : attributesToUpdate ) 
			{
				if ( currentAttribute.getId() == attributeToUpdate.getId() ) found = true;
			}
			
			if ( !found )
			{
				//Iremos apagar os attributos e as op��es 
				if ( currentAttribute.getType().equals(AttributeType.MULTIPLE_CHOICE) )
				{
					this.attributeOptionRepository.delete( currentAttribute.getOptions() );
					this.attributeOptionRepository.flush();
				}
				this.attributeRepository.delete( currentAttribute );
				this.attributeRepository.flush();
			}
		}
		
		//Agora que j� apagamos todos os attributos, devemos inserir/atualizar os que est�o vindo com o layer
		for ( Attribute attributeToUpdate : attributesToUpdate ) 
		{
			List<AttributeOption> attributesOptionToUpdate = attributeToUpdate.getOptions();
			
			attributeToUpdate = this.attributeRepository.save( attributeToUpdate );
			
			for ( Attribute currentAttribute : currentSavedAttributes ) 
			{
				if ( attributeToUpdate.getId() == currentAttribute.getId() )
				{
					if ( attributeToUpdate.getType().equals(AttributeType.MULTIPLE_CHOICE) )
					{
						//Devemos verificar os options do attributo
						for (AttributeOption currentAttributeOption : currentAttribute.getOptions() ) 
						{
							boolean attributeOptionFound = false;
							for (AttributeOption attributeOption : attributesOptionToUpdate) 
							{
								attributeOption.setAttribute(attributeToUpdate);
								if ( attributeOption.getId() == currentAttributeOption.getId() ) attributeOptionFound = true;
							}
							
							if ( !attributeOptionFound ) 
							{
								this.attributeOptionRepository.delete(currentAttributeOption);
								this.attributeOptionRepository.flush();
							}
						}
						//Apagamos todos os removidos, agora devemos inserir/alterar os do front
						this.attributeOptionRepository.save(attributesOptionToUpdate );
					}
				}
			}
			
			
		}
		
		
		layer = this.layerRepository.save( layer );
		
		return layer;
		

	}
	
	/**
	 * m�todo para remover uma {@link Camada}
	 * 
	 * @param id
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	public void removeLayer( Long id )
	{	
		List<AccessGroupLayer> layers = this.accessGroupLayerRepository.listByLayerId(id);
		for (AccessGroupLayer accessGroupLayer : layers)
		{
			this.accessGroupLayerRepository.delete(accessGroupLayer);
		}
		
		try
		{	
			List<Attribute> attributesToRemove = attributeRepository.listAttributeByLayerMarker(id);
			
			for (Attribute attribute : attributesToRemove) 
			{
				this.attributeOptionRepository.delete(this.attributeOptionRepository.listByAttributeId(attribute.getId()));
				this.attributeRepository.delete(attribute);
				this.attributeRepository.flush();
			}
			
			this.layerRepository.delete( id );			
		}
		catch (ConstraintViolationException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * m�todo para encontrar uma {@link Camada} pelo id
	 * 
	 * @param id
	 * @return camada
	 * @throws JAXBException 
	 */
	@Transactional(readOnly = true)
	public Layer findLayerById( Long id )
	{		
		final Layer layer = this.layerRepository.findById(id);
		
		layer.setAttributes(this.attributeRepository.listAttributeByLayerMarker(id));
		
		for ( Attribute attribute : layer.getAttributes() ) 
		{
			if ( attribute.getType().equals(AttributeType.MULTIPLE_CHOICE) )
			{
				attribute.setOptions( this.attributeOptionRepository.listByAttributeId(attribute.getId() ) );
			}
		}
				
		// traz a legenda da camada do GeoServer
		if( layer.getDataSource().getUrl() != null ) {
			layer.setLegend(getLegendLayerFromGeoServer(layer));	
		}
		
		return layer;
	}
	
	/**
	 * M�todo para listar as configura��es de camadas paginadas com op��o do filtro
	 *
	 * @param filter
	 * @param pageable
	 * @return
	 * @throws JAXBException 
	 */
	@Transactional(readOnly=true)
	public Page<Layer> listLayersConfigurationByFilter( String filter, Long dataSourceId, PageRequest pageable )
	{
		Page<Layer> layers = this.layerRepository.listByFilters(filter, dataSourceId, pageable);
		
		for ( Layer layer : layers.getContent() )
		{
			// traz a legenda da camada do GeoServer
			if( layer.getDataSource().getUrl() != null) {
				layer.setLegend(getLegendLayerFromGeoServer(layer));
			}
		}
		
		return layers;
	}
	
	
	/**
	 * M�todo que busca a legenda de uma camada no geo server
	 * @param camada
	 * @return
	 */
	public String getLegendLayerFromGeoServer( Layer layer )
	{
		int position = layer.getDataSource().getUrl().lastIndexOf("ows?");
		String urlGeoserver = layer.getDataSource().getUrl().substring(0, position);
		
		return urlGeoserver + Layer.LEGEND_GRAPHIC_URL + layer.getName() + Layer.LEGEND_GRAPHIC_FORMAT;
	}
	
	
	/**
	 * 
	 * @param accessGroup
	 * @param camadaId
	 */
	public void linkAccessGroup( List<AccessGroup> accessGroups, Long layerId )
	{
		Layer layer = new Layer(layerId);
		for (AccessGroup accessGroup : accessGroups)
		{
			AccessGroupLayer accesGroupLayer = new AccessGroupLayer();
			accesGroupLayer.setAccessGroup(accessGroup);
			accesGroupLayer.setLayer(layer);
			
			this.accessGroupLayerRepository.save(accesGroupLayer);
		}
	}
	
	/**
	 * 
	 * @param accessGroup
	 * @param pesquisaPersonalizada
	 */
	public void unlinkAccessGroup( List<AccessGroup> accessGroups, Long layerId )
	{
		for (AccessGroup accessGroup : accessGroups)
		{
			List<AccessGroupLayer> list = this.accessGroupLayerRepository.listByAccessGroupLayerId(accessGroup.getId(), layerId);
			for (AccessGroupLayer accessGroupLayer : list)
			{
				this.accessGroupLayerRepository.delete(accessGroupLayer);
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=true)
	public ArrayList<AccessGroup> listAccessGroupByLayerId(Long id)
	{
		ArrayList<AccessGroup> grupos = new ArrayList<AccessGroup>();
		
		List<AccessGroupLayer> grupoAcessoCamadas = this.accessGroupLayerRepository.listByLayerId(id);
		
		for (AccessGroupLayer grupoAcessoCamada : grupoAcessoCamadas)
		{
			grupoAcessoCamada.setAccessGroup(this.accessGroupRepository.findById(grupoAcessoCamada.getAccessGroup().getId()));
			grupos.add(grupoAcessoCamada.getAccessGroup());
		}
		
		return grupos;
	}
	
	
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public List<String> listAllFeatures(List<String>  listUrls)
	{
		this.template = new RestTemplate();
		List<String> listFeatures = new ArrayList<String>();
		
		for (String url : listUrls)
		{
			try
			{
				listFeatures.add(this.template.getForObject(url, String.class));
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage()	);
				continue;
			}
		}
		
		return listFeatures;
	}
	
	
	/**
	 * 
	 * @param grupoCamadas
	 */
	@PreAuthorize("hasRole('"+UserRole.ADMINISTRATOR_VALUE+"')")
	private void removeLayersGroupPublishedEmpty( LayerGroup layerGroup )
	{
		if ( layerGroup.getLayersGroup() != null )
		{
			List<LayerGroup> layersGroupExclusion = new ArrayList<LayerGroup>();
			
			for (LayerGroup layerGroupChild : layerGroup.getLayersGroup())
			{
				removeLayersGroupPublishedEmpty(layerGroupChild);
				
				// remove o grupo de camada publicado superior vazio
				if (layerGroupChild.getLayersGroup().isEmpty() && layerGroupChild.getLayers().isEmpty())
				{
					layersGroupExclusion.add(layerGroupChild);
				}
				
			}
			
			layerGroup.getLayersGroup().removeAll(layersGroupExclusion);
		}
		
	}
	
	/**
	 * @param layerId
	 * @return
	 */
	public List<Attribute> listAttributesByLayer(Long layerId){
		final List<Attribute> attributes =  this.attributeRepository.listAttributeByLayer(layerId);
		
		for ( Attribute attribute : attributes ) 
		{
			if ( attribute.getType().equals(AttributeType.MULTIPLE_CHOICE) )
			{
				attribute.setOptions( this.attributeOptionRepository.listByAttributeId(attribute.getId() ) );
			}
		}
		
		return attributes;
	}
	
	/**
	 * Method that return a list of tools by user access group
	 */
	public List<Tool> listToolsByUser()
	{
		List<Tool> toolsUser = new ArrayList<Tool>();
		final User user = ContextHolder.getAuthenticatedUser();
		
		//logado
		if (!user.equals(User.ANONYMOUS)) {
			List<AccessGroup> accessGroupsUser = this.accessGroupRepository.listByUser(user.getUsername());
			
			if (user.getRole() != UserRole.ADMINISTRATOR)
			{				
								
				for (AccessGroup accessGroup : accessGroupsUser)
				{
					accessGroup = this.accessGroupRepository.findOne(accessGroup.getId());
					
					for (Tool tool : accessGroup.getTools())
					{
						toolsUser.add(tool);
					}
				}								
				
			} else {
				toolsUser = this.toolRepository.findAll();
			}
			
		} else{
			
			AccessGroup accessGroup = this.accessGroupRepository.findOne(AccessGroup.PUBLIC_GROUP_ID);
			
			accessGroup = this.accessGroupRepository.findOne(accessGroup.getId());
				
			for (Tool tool : accessGroup.getTools())
			{
				toolsUser.add(tool);
			}
			
		}	
		
		return toolsUser ;
	}
	
	/**
	 * Verifica se os grupos de acesso de uma p�gina de grupos de acessos tem filhos
	 * @param listByFilter
	 * @return
	 */
	private Page<LayerGroup> hasChildren(Page<LayerGroup> layersGroups)
	{
		for (LayerGroup layerGroup : layersGroups)
		{
			layerGroup = this.hasChildren(layerGroup);
		}
		return layersGroups;
	}
	
	/**
	 * Verifica se os grupos de acesso de uma lista de grupos de acessos tem filhos
	 * @param accessGroups
	 * @param published
	 * @return
	 */
	private List<LayerGroup> hasChildren(List<LayerGroup> layersGroups)
	{
		for (LayerGroup layerGroup : layersGroups)
		{
			layerGroup = this.hasChildren(layerGroup);
		}
		return layersGroups;
	}
	
	/**
	 * Verifica se o grupo de acesso tem filhos
	 * @param accessGroups
	 * @return
	 */
	private LayerGroup hasChildren(LayerGroup layerGroup)
	{		
		
		List<LayerGroup> listLayerGroups = this.layerGroupRepository.listLayersGroupByLayerGroupId(layerGroup.getId());
		List<Layer> layers = this.layerRepository.listLayersByLayerGroupId(layerGroup.getId());
//		this.setIcon(layers);
		
		layerGroup.setLayers(layers);
		// Verifica se tem filhos, sejam layersGroups ou layers
		layerGroup.setHasChildren((listLayerGroups != null && listLayerGroups.size() >0 || layers != null && layers.size() >0) 
				|| ((listLayerGroups != null && listLayerGroups.size() >0 && layers != null && layers.size() >0)));
		
		if (layerGroup.getHasChildren())
		{
			layerGroup.setLayersGroup(hasChildren(listLayerGroups));
		}
		return layerGroup;
	}
	
	
}
