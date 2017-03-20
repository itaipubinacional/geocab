/**
 *
 */
package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.application.security.ContextHolder;
import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroup;
import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroupLayer;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.configuration.account.UserRole;
import br.gov.itaipu.geocab.domain.entity.datasource.DataSource;
import br.gov.itaipu.geocab.domain.entity.layer.*;
import br.gov.itaipu.geocab.domain.entity.tool.Tool;
import br.gov.itaipu.geocab.domain.repository.accessgroup.AccessGroupLayerRepository;
import br.gov.itaipu.geocab.domain.repository.accessgroup.AccessGroupRepository;
import br.gov.itaipu.geocab.domain.repository.layer.AttributeRepository;
import br.gov.itaipu.geocab.domain.repository.layer.LayerGroupRepository;
import br.gov.itaipu.geocab.domain.repository.layer.LayerRepository;
import br.gov.itaipu.geocab.domain.repository.tool.ToolRepository;
import br.gov.itaipu.geocab.infrastructure.file.FileRepository;
import br.gov.itaipu.geocab.infrastructure.geoserver.GeoserverConnection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletContext;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category Service
 * @since 22/09/2014
 */
@Service
@Transactional
public class LayerGroupService {
    /*-------------------------------------------------------------------
     * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    @Autowired
    private LayerGroupRepository layerGroupRepository;

    /**
     *
     */
    @Autowired(required = false)
    private ServletContext servletContext;

    /**
     *
     */
    @Autowired
    private LayerRepository layerRepository;

    /**
     *
     */
    @Autowired
    private AccessGroupLayerRepository accessGroupLayerRepository;

    /**
     *
     */
    @Autowired
    private AccessGroupRepository accessGroupRepository;

    /**
     *
     */
    @Autowired
    private AttributeRepository attributeRepository;

    /**
     *
     */
    @Autowired
    private ToolRepository toolRepository;

    /**
     *
     */
    protected RestTemplate template;

    //files
    /**
     *
     */
    @Autowired
    private FileRepository fileRepository;

	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @return
     */
    public List<String> listLayersIcons() {
        final String path = this.servletContext.getRealPath("/static/icons");

        File[] files = this.fileRepository.listFilePath(path);

        List<String> layers = new ArrayList<String>();

        for (File file : files) {
            if (file.isFile()) {
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
    
    public LayerGroup insertLayerGroup(LayerGroup layerGroup) {
        layerGroup.setPublished(false);
        return hasChildren(this.layerGroupRepository.save(layerGroup));
    }

    /**
     * Method to update an {@link LayerGroup}
     *
     * @param layerGroup
     * @return layerGroup
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public LayerGroup updateLayerGroup(LayerGroup layerGroup) {
        return hasChildren(this.layerGroupRepository.save(layerGroup));
    }


    /**
     * Find {@link LayerGroup} by id
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public LayerGroup findLayerGroupById(Long id) {
        return hasChildren(this.layerGroupRepository.findOne(id));
    }

    /**
     *
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void saveAllParentLayerGroup(List<LayerGroup> layerGroup) {
        List<LayerGroup> layersGroups = this.layerGroupRepository.listAllParentLayerGroup();

        for (int i = 0; i < layersGroups.size(); i++) {
            layersGroups.get(i).setOrderLayerGroup(i);
            this.layerGroupRepository.save(layersGroups.get(i));
        }
    }


    /**
     * Método que seta todos os grupos publicados filhos em seus respectivos grupos publicados pai
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    private void populateChildrenInLayerGroupPublished() {
        final List<LayerGroup> layersGroupPublished = this.layerGroupRepository.listAllLayersGroupPublished();

        for (LayerGroup layerGroupPublished : layersGroupPublished) {
            //Pega o grupo de camadas pois a query anterior (layerGroupRepository.listAllLayersGroupPublished()) foi alterada e não pega todos os atributos da camada
            layerGroupPublished = this.layerGroupRepository.findOne(layerGroupPublished.getId());

            layerGroupPublished.setLayersGroup(this.layerGroupRepository.listLayersGroupPublishedChildren(layerGroupPublished.getId()));

            this.layerGroupRepository.save(layerGroupPublished);
        }
    }


    /**
     * Método recursivo que remove os grupos de camadas publicados filhos
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    private void removeLayerGroupPublished(LayerGroup layerGroupPublished) {
        if (layerGroupPublished.getLayersGroup() != null) {
            for (LayerGroup layerGroupChildPublished : layerGroupPublished.getLayersGroup()) {
                // remove quando o rascunho for null e o publicado is true
                if (layerGroupChildPublished.getPublished() && layerGroupChildPublished.getDraft() == null) {
                    removeLayerGroupPublished(layerGroupChildPublished);
                    this.layerGroupRepository.delete(layerGroupChildPublished);
                }
            }
        }

    }


    /**
     *
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void recursive(LayerGroup layerGroupOriginal, LayerGroup layerGroupUpperPublished) {
        final Long layerGroupOriginalId = layerGroupOriginal.getId();

        // verifica se foi possui o grupo publicado
        final LayerGroup layerGroupPublishedExistent = this.layerGroupRepository.findByDraftId(layerGroupOriginalId);

        // efetua a cópia do grupo de camadas original
        LayerGroup layerGroupPublished = new LayerGroup();
        BeanUtils.copyProperties(layerGroupOriginal, layerGroupPublished);

        // update nos dados do grupo publicado
        layerGroupPublished.setPublished(true);
        layerGroupPublished.setLayerGroupUpper(layerGroupUpperPublished);
        layerGroupPublished.setDraft(new LayerGroup(layerGroupOriginalId));
        layerGroupPublished.setLayersGroup(new ArrayList<LayerGroup>());
        layerGroupPublished.setLayers(new ArrayList<Layer>());

        // se já possui o grupo criado apenas altera o existente sentido cria o grupo publicado
        if (layerGroupPublishedExistent != null) {
            layerGroupPublished.setId(layerGroupPublishedExistent.getId());
            layerGroupPublished = this.layerGroupRepository.save(layerGroupPublished);
        } else {
            layerGroupPublished.setId(null);
            layerGroupPublished = this.layerGroupRepository.save(layerGroupPublished);
        }

        // criação atualização de camadas para camadas publicadas
        if (layerGroupOriginal.getLayers() != null) {
            for (Layer layerOriginal : layerGroupOriginal.getLayers()) {
                //final Camada camadaPublicadaExistente = this.camadaRepository.findByRascunhoId(camadaOriginal.getId());

                // criação da camada publicada que iria conter a ordem publicada e os grupos
                Layer layerPublished = new Layer();
//				BeanUtils.copyProperties(camadaOriginal, camadaPublicada);


                // criação/update na camada publicada
                layerPublished.setName(layerOriginal.getName());
                layerPublished.setTitle(layerOriginal.getTitle());
                layerPublished.setIcon(layerOriginal.getIcon());
                layerPublished.setDataSource(new DataSource(layerOriginal.getDataSource().getId()));
                layerPublished.setMinimumScaleMap(layerOriginal.getMinimumScaleMap());
                layerPublished.setMaximumScaleMap(layerOriginal.getMaximumScaleMap());
                layerPublished.setOrderLayer(layerOriginal.getOrderLayer());
                layerPublished.setLayerGroup(layerGroupPublished);
                layerPublished.setPublished(true);

                // se já possui a camada publicada apenas altera a existente sentido cria a camada publicada
                if (layerOriginal.getPublishedLayer() != null) {
                    layerPublished.setId(layerOriginal.getPublishedLayer().getId());
                } else {
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
        if (layerGroupPublished.getLayersGroup() != null) {
            if (layerGroupOriginal.getLayersGroup() != null) {
                for (LayerGroup layerGroupOriginalChild : layerGroupOriginal.getLayersGroup()) {
                    this.recursive(layerGroupOriginalChild, layerGroupPublished);
                }
            }
        }
    }

    /**
     *
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void publishLayerGroup(List<LayerGroup> layersGroup) {
        this.saveAllLayersGroup(layersGroup); // save layersGroup

        final List<LayerGroup> layerGroupOriginals = this.listLayersGroupUpper();//list parent groups

        for (LayerGroup layerGroupOriginal : layerGroupOriginals) {
            this.recursive(layerGroupOriginal, null);//recursion to insert or update a layers group published
        }

        //exclude published groups
        //set children groups in correct parent group
        this.populateChildrenInLayerGroupPublished();

        final List<LayerGroup> layersGroupPublished = this.layerGroupRepository.listLayersGroupUpperPublished();


        if (layersGroupPublished != null) {
            for (LayerGroup layerGroupPublished : layersGroupPublished) {
                this.removeLayerGroupPublished(layerGroupPublished);

                // remove o grupo de camada publicado superior
                // remove quando o rascunho for null e o publicado is true
                if (layerGroupPublished.getPublished() && layerGroupPublished.getDraft() == null) {
                    this.layerGroupRepository.delete(layerGroupPublished);
                }
            }
        }
    }

    /**
     * Method to save a list of {@link LayerGroup}
     *
     * @return
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void saveAllLayersGroup(List<LayerGroup> layerGroup) {
        this.prioritizeLayersGroup(layerGroup, null);

        this.prioritizeLayers(layerGroup);
    }

    /**
     * @param layerGroups
     * @param layerGroupUpper
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    private void prioritizeLayersGroup(List<LayerGroup> layerGroups, LayerGroup layerGroupUpper) {
        if (layerGroups != null) {
            for (int i = 0; i < layerGroups.size(); i++) {
                layerGroups.get(i).setOrderLayerGroup(i);
                layerGroups.get(i).setLayerGroupUpper(layerGroupUpper);

                this.layerGroupRepository.save(layerGroups.get(i));
                prioritizeLayersGroup(layerGroups.get(i).getLayersGroup(), layerGroups.get(i));
            }
        }
    }

    /**
     * @param layerGroups
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    private void prioritizeLayers(List<LayerGroup> layerGroups) {
        for (LayerGroup layerGroup : layerGroups) {
            if (layerGroup.getLayers() != null) {
                for (int j = 0; j < layerGroup.getLayers().size(); j++) {
                    layerGroup.getLayers().get(j).setOrderLayer(j);
                    layerGroup.getLayers().get(j).setLayerGroup(layerGroup);

                    this.layerRepository.save(layerGroup.getLayers().get(j));
                }
                if (layerGroup.getLayersGroup() != null) prioritizeLayers(layerGroup.getLayersGroup());
            }
        }
    }

    /**
     * Método para remover um {@link LayerGroup}
     *
     * @param id
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void removeLayerGroup(Long id) {
        final LayerGroup layerGroup = this.layerGroupRepository.findOne(id);

        LayerGroup layerGroupPublished = this.layerGroupRepository.findByDraftId(id);

        // verifica se existe o grupo já publicado
        if (layerGroupPublished != null) {
            // seta null no campo rascunho do grupo de camada publicado para permitir excluir o grupo original
            layerGroupPublished.setDraft(null);
            this.layerGroupRepository.save(layerGroupPublished);
        }

        this.layerGroupRepository.delete(layerGroup);
    }

    /**
     * @return
     */
    @Transactional(readOnly = true)
    public List<LayerGroup> listLayersGroupUpper() {
        List<LayerGroup> layersGroup = this.layerGroupRepository.listLayersGroupUpper();

        List<LayerGroup> listLayersGroups = hasChildren(layersGroup);

        for (LayerGroup layerGroup : listLayersGroups) {
            for (Layer layer : layerGroup.getLayers()) {
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
    @Transactional(readOnly = true)
    public LayerGroup listLayersGroupPublishedByLayerGroupId(Long id) {
        return hasChildren(this.listLayersGroupByLayerGroupId(id, true));
    }

    /**
     * Lista os Grupos de camadas não publicados
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public LayerGroup listLayersGroupByLayerGroupId(Long id) {
        return hasChildren(this.listLayersGroupByLayerGroupId(id, false));
    }

    /**
     * Lista os Grupos de camadas
     *
     * @param id
     * @param published
     * @return
     */
    @Transactional(readOnly = true)
    public LayerGroup listLayersGroupByLayerGroupId(Long id, Boolean published) {

        LayerGroup layerGroup = this.layerGroupRepository.findLayerGroupById(id);

        List<LayerGroup> layersGroup = this.layerGroupRepository.listLayersGroupByLayerGroupId(id, published);

        //Se o grupo de camadas está vazio (se não tem outros grupos de camadas internamente) pega as camadas desse grupo de camadas
        if (layersGroup.isEmpty()) {

            List<Layer> layers = this.layerRepository.listLayersByLayerGroupId(id, published);

            layerGroup.setLayers(layers);

        }
        //Se o grupo de camadas tem outros grupos de camadas internamente, seta o grupo de camadas
        else {
            layerGroup.setLayersGroup(layersGroup);
        }
        return hasChildren(layerGroup);
    }


    /**
     * Método que retorna a estrutura completa dos grupos de camadas publicados
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<LayerGroup> listLayerGroupUpperPublished() {
        List<LayerGroup> layersGroupUpperPublished = new ArrayList<LayerGroup>();
        final List<LayerGroup> layersGroupPublished = this.layerGroupRepository.listAllLayersGroupPublished();

        if (layersGroupPublished != null) {
            for (LayerGroup layerGroupPublished : layersGroupPublished) {
                layerGroupPublished.setLayers(this.layerRepository.listLayersByLayerGroupPublished(layerGroupPublished.getId()));

                if (layerGroupPublished.getLayerGroupUpper() == null || layerGroupPublished.getLayerGroupUpper().getId() == null) {
                    layersGroupUpperPublished.add(layerGroupPublished);
                }
            }
        }


        for (LayerGroup layerGroup : layersGroupPublished) {
            for (Layer layer : layerGroup.getLayers()) {
                this.setIcon(layer);
            }
        }
        //Se o usuário for administrador, ele poderá visualizar todas os grupos de acesso.

        return this.layersGroupUpperByRole(layersGroupUpperPublished);

    }


    /**
     * @param layersGroupUpperPublished
     * @return
     */
    private List<LayerGroup> layersGroupUpperByRole(List<LayerGroup> layersGroupUpperPublished) {

        List<AccessGroup> accessGroupsUser = new ArrayList<AccessGroup>();
        final User user = ContextHolder.getAuthenticatedUser();

        if (!user.equals(User.ANONYMOUS)) {
            if (user.getRole() != UserRole.ADMINISTRATOR) {
                accessGroupsUser = this.accessGroupRepository.listByUser(user.getEmail());

                for (AccessGroup accessGroup : accessGroupsUser) {
                    accessGroup.setAccessGroupLayer(new HashSet<AccessGroupLayer>(this.accessGroupLayerRepository.listByAccessGroupId(accessGroup.getId())));
                }

                if (!layersGroupUpperPublished.isEmpty()) {
                    verifyLayerPermission(layersGroupUpperPublished, accessGroupsUser);
                }

                List<LayerGroup> layerGroupToDelete = new ArrayList<LayerGroup>();

                for (LayerGroup layerGroup : layersGroupUpperPublished) {
                    this.removeLayerGroupEmptyPublished(layerGroup);

                    if (layerGroup.getLayersGroup().isEmpty() & layerGroup.getLayers().isEmpty()) {
                        layerGroupToDelete.add(layerGroup);
                    }
                }
                layersGroupUpperPublished.removeAll(layerGroupToDelete);
            }
        } else {
            AccessGroup accessGroup = this.accessGroupRepository.findById(AccessGroup.PUBLIC_GROUP_ID);
            accessGroupsUser.add(accessGroup);

            accessGroup.setAccessGroupLayer(new HashSet<AccessGroupLayer>(this.accessGroupLayerRepository.listByAccessGroupId(accessGroup.getId())));

            if (!layersGroupUpperPublished.isEmpty()) {
                verifyLayerPermission(layersGroupUpperPublished, accessGroupsUser);
            }

            List<LayerGroup> layerGroupToDelete = new ArrayList<LayerGroup>();

            for (LayerGroup layerGroup : layersGroupUpperPublished) {
                this.removeLayerGroupEmptyPublished(layerGroup);

                if (layerGroup.getLayersGroup().isEmpty() & layerGroup.getLayers().isEmpty()) {
                    layerGroupToDelete.add(layerGroup);
                }
            }
            layersGroupUpperPublished.removeAll(layerGroupToDelete);
        }

        return layersGroupUpperPublished;

    }

    /**
     *
     */
    private void verifyLayerPermission(List<LayerGroup> layerGroups, List<AccessGroup> accessGroupUser) {
        boolean hasAccess = false;

        if (layerGroups != null) {
            if (!layerGroups.isEmpty()) {
                for (LayerGroup group : layerGroups) {
                    if (group.getLayers() != null) {
                        if (group.getLayers().size() > 0) {
                            List<Layer> layersToDelete = new ArrayList<Layer>();

                            for (Layer layer : group.getLayers()) {
                                if (!accessGroupUser.isEmpty()) {
                                    for (AccessGroup accessGroup : accessGroupUser) {
                                        if (!accessGroup.getAccessGroupLayer().isEmpty()) {
                                            for (AccessGroupLayer accessGroupLayer : accessGroup.getAccessGroupLayer()) {
                                                if (layer.getId().equals(accessGroupLayer.getLayer().getId())) {
                                                    hasAccess = true;
                                                    break;
                                                } else {
                                                    hasAccess = false;
                                                }
                                            }
                                        }

                                        if (hasAccess) {
                                            break;
                                        }
                                    }
                                }

                                if (!hasAccess) {
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
     */
    private void removeLayerGroupEmptyPublished(LayerGroup layerGroups) {
        if (layerGroups.getLayersGroup() != null) {
            List<LayerGroup> layerGroupToDelete = new ArrayList<LayerGroup>();

            for (LayerGroup layerGroupChildren : layerGroups.getLayersGroup()) {

                removeLayerGroupEmptyPublished(layerGroupChildren);

                // remove o grupo de camada publicado superior vazio
                if (layerGroupChildren.getLayersGroup().isEmpty() & layerGroupChildren.getLayers().isEmpty()) {
                    layerGroupToDelete.add(layerGroupChildren);
                }

            }

            layerGroups.getLayersGroup().removeAll(layerGroupToDelete);
        }

    }

    /**
     * Traz a legenda da camada do GeoServer
     *
     * @param layer
     * @return
     */
    private Layer setIcon(Layer layer) {
        if (layer.getDataSource() != null && layer.getDataSource().getUrl() != null) {
            layer.setLegend((getLegendLayerFromGeoServer(layer)));
            layer.setIcon((layer.getLegend()));
        }
        return layer;
    }

    /**
     * @param filter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<LayerGroup> listLayerGroups(String filter, PageRequest pageable) {
        return this.hasChildren(this.layerGroupRepository.listByFilter(filter, pageable));
    }


    /**
     * Não é necessária a sincronização com hasChildren
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<LayerGroup> getLayerGroups() {
        return this.layerGroupRepository.findAll();
    }

    /**
     * @return camadas filtradas pelos grupos de acesso do usuário
     */
    @Transactional(readOnly = true)
    public List<Layer> getInternalLayerGroups() {
        User user = ContextHolder.getAuthenticatedUser();
        if (user.getRole().equals(UserRole.ADMINISTRATOR)) {
            return this.layerRepository.listAllInternalLayerGroups();
        }
        return this.layerRepository.listAllInternalLayerGroupsAndByUser(user.getId());
    }

    /**
     * @return
     */
    @Transactional(readOnly = true)
    public List<Layer> getInternalLayers() {
        return this.layerRepository.listAllInternalLayers();
    }

    /**
     * @return
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public List<ExternalLayer> getExternalLayers(DataSource dataSource) throws Exception {
        GeoserverConnection conn = GeoserverConnection.createConnection(dataSource);
        return conn.getExternalLayers();

    }

    /**
     * @param layer
     * @param dataSource
     * @return
     */
    @Transactional(readOnly = true)
    public List<LayerGroup> listSupervisorsFilter(String layer, Long dataSource) {
        List<LayerGroup> layersGroup = this.layerGroupRepository.listSupervisorsFilter(layer, dataSource);

        return hasChildren(layersGroup);

    }


    /**
     * Retorna a lista de campos configurados em uma camada.
     *
     * @param layer A camada a ser pesquisada.
     * @return Retorna a lista de campos da camada.
     * @throws Exception Lança exceção caso ocorra algum problema durante a
     *                   consulta.
     */
    @Transactional(readOnly = true)
    public List<LayerField> getLayerFields(Layer layer) throws Exception {
        // se for uma camada interna
        if (layer.getDataSource().getUrl() == null)
            return getInternalLayerFields(layer);
        else
            return getExternalLayerFields(layer);
    }

    /**
     * Retorna a lista de campos configurados em uma camada interna.
     *
     * @param layer A camada a ser pesquisada.
     * @return Retorna a lista de campos da camada interna. Se a camada passada não
     * for interna, será retornado <code>null</code>.
     */
    private List<LayerField> getInternalLayerFields(Layer layer) {
        // se a fonte de dados da camada não for interna
        if (layer.getDataSource().getUrl() != null)
            return null;

        List<LayerField> layerFields = new ArrayList<>();
        List<Attribute> attrs = this.listAttributesByLayer(layer.getId());

        for (Attribute attr : attrs) {
            LayerField layerField = new LayerField();
            layerField.setName(attr.getName());
            layerField.setAttributeId(attr.getId());

            if (attr.getType() == AttributeType.TEXT) {
                layerField.setType(LayerFieldType.STRING);
            } else if (attr.getType() == AttributeType.DATE) {
                layerField.setType(LayerFieldType.DATE);
            } else if (attr.getType() == AttributeType.NUMBER) {
                layerField.setType(LayerFieldType.INT);
            } else if (attr.getType() == AttributeType.BOOLEAN) {
                layerField.setType(LayerFieldType.BOOLEAN);
            }

            layerFields.add(layerField);

        }

        return layerFields;
    }

    /**
     * Retorna a lista de campos configurados em uma camada externa. Esta lista será consultada
     * através do serviço WFS da fonte de dados configurada.
     *
     * @param layer A camada a ser pesquisada.
     * @return Retorna a lista de campos da camada externa. Caso ocorrer um problema na comunicação
     * com o serviço WFS ou a camada passada não for externa, será retornado <code>null</code>.
     * @throws Exception Lança exceção em caso de problema com a conexão com o Geoserver.
     */
    private List<LayerField> getExternalLayerFields(Layer layer) throws Exception {
        // cria uma conexão com o Geoserver e faz a consulta
        GeoserverConnection conn = GeoserverConnection.createConnection(layer.getDataSource());
        return conn.getLayerFields(layer.getName());
    }


    /**
     * Método responsável para listar as camadas
     *
     * @param filter
     * @param pageable
     * @return camadas
     */
//	@Transactional(readOnly=true)
    public Page<Layer> listLayersByFilters(String filter, Long dataSourceId, PageRequest pageable) {
        Page<Layer> layers = null;

        User user = ContextHolder.getAuthenticatedUser();

        if (user.getRole().equals(UserRole.ADMINISTRATOR)) {
            layers = this.layerRepository.listByFilters(filter, dataSourceId, pageable); //TODO porque o dataSourceId esta entrnando null?
        } else {
            layers = this.layerRepository.listByFiltersAndByUser(filter, dataSourceId, user.getId(), pageable); //TODO porque o dataSourceId esta entrnando null?
        }


        for (Layer layer : layers.getContent()) {
            // traz a legenda da camada do GeoServer
            if (layer.getDataSource() != null && layer.getDataSource().getUrl() != null) {
                layer.setLegend(getLegendLayerFromGeoServer(layer));
            }
        }

        return layers;

    }

    /**
     * @param filter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Layer> listLayersByFilters(String filter, PageRequest pageable) {
        Page<Layer> layers = null;

        User user = ContextHolder.getAuthenticatedUser();

        if (user.getRole().equals(UserRole.ADMINISTRATOR)) {
            layers = this.layerRepository.listByFilters(filter, null, pageable); //TODO porque o dataSourceId esta entrnando null?
        } else {
            layers = this.layerRepository.listByFiltersAndByUser(filter, null, user.getId(), pageable); //TODO porque o dataSourceId esta entrnando null?
        }

        for (Layer layer : layers.getContent()) {
            // traz a legenda da camada do GeoServer
            if (layer.getDataSource() != null && layer.getDataSource().getUrl() != null) {
                layer.setLegend(getLegendLayerFromGeoServer(layer));
            }
        }

        return layers;
    }

    /**
     * @return
     */
    @Transactional(readOnly = true)
    public List<Layer> listLayersPublished() {
        return this.layerRepository.listLayersPublished();
    }

    /**
     * Method to inserir uma {@link Layer}
     *
     * @param layer
     * @return
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public Layer insertLayer(Layer layer) {

        layer.setLayerGroup(this.layerGroupRepository.findOne(layer.getLayerGroup().getId()));
        layer.setPublished(false);
        layer.setEnabled(layer.getEnabled() == null ? false : layer.getEnabled());
        //Valida se os atributos são válidos
        layer.validate();

        this.layerRepository.save(layer);

        List<Attribute> attributies = layer.getAttributes();
        layer.setAttributes(this.attributeRepository.save(attributies));

        return layer;
    }

    /**
     * método para atualizar uma {@link Layer}
     *
     * @param layer
     * @return camada
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public Layer updateLayer(Layer layer) {
        layer.setLayerGroup(layer.getLayerGroup());

        List<Attribute> attributesToDelete = attributeRepository.listAttributeByLayer(layer.getId());

        attributesToDelete.removeAll(layer.getAttributes());

        for (Attribute attribute : attributesToDelete) {
            this.attributeRepository.delete(attribute);
        }
		
		/* Na atualização não foi permitido modificar a fonte de dados, camada e título, dessa forma, 
		Os valores originais são mantidos. */
        Layer layerDatabase = this.findLayerById(layer.getId());
        layer.setDataSource(layerDatabase.getDataSource());
        layer.setName(layerDatabase.getName());
        layer.setEnabled(layer.getEnabled() == null ? false : layer.getEnabled());

        for (Attribute attribute : layer.getAttributes()) {
            this.attributeRepository.save(attribute);
        }

        if (layer.getPublishedLayer() != null) {
            layer.setPublishedLayer(layerRepository.findById(layer.getPublishedLayer().getId()));
        }
        return this.layerRepository.save(layer);

    }

    /**
     * método para remover uma {@link Layer}
     *
     * @param id
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void removeLayer(Long id) {
        List<AccessGroupLayer> layers = this.accessGroupLayerRepository.listByLayerId(id);
        for (AccessGroupLayer accessGroupLayer : layers) {
            this.accessGroupLayerRepository.delete(accessGroupLayer);
        }

        try {
            this.attributeRepository.delete(attributeRepository.listAttributeByLayerMarker(id));
            this.layerRepository.delete(id);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
        }

    }

    /**
     * método para encontrar uma {@link Layer} pelo id
     *
     * @param id
     * @return camada
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public Layer findLayerById(Long id) {
        final Layer layer = this.layerRepository.findById(id);

        layer.setAttributes(this.attributeRepository.listAttributeByLayerMarker(id));

        // traz a legenda da camada do GeoServer
        if (layer.getDataSource().getUrl() != null) {
            layer.setLegend(getLegendLayerFromGeoServer(layer));
        }

        return layer;
    }

    /**
     * Método para listar as configurações de camadas paginadas com opção do filtro
     *
     * @param filter
     * @param pageable
     * @return
     * @throws JAXBException
     */
    @Transactional(readOnly = true)
    public Page<Layer> listLayersConfigurationByFilter(String filter, Long dataSourceId, PageRequest pageable) {
        Page<Layer> layers = this.layerRepository.listByFilters(filter, dataSourceId, pageable);

        for (Layer layer : layers.getContent()) {
            // traz a legenda da camada do GeoServer
            if (layer.getDataSource().getUrl() != null) {
                layer.setLegend(getLegendLayerFromGeoServer(layer));
            }
        }

        return layers;
    }


    /**
     * Método que busca a legenda de uma camada no geo server
     *
     * @param layer
     * @return
     */
    public String getLegendLayerFromGeoServer(Layer layer) {
        int position = layer.getDataSource().getUrl().lastIndexOf("ows?");
        String urlGeoserver = layer.getDataSource().getUrl().substring(0, position);

        return urlGeoserver + Layer.LEGEND_GRAPHIC_URL + layer.getName() + Layer.LEGEND_GRAPHIC_FORMAT;
    }


    /**
     *
     */
    public void linkAccessGroup(List<AccessGroup> accessGroups, Long layerId) {
        Layer layer = new Layer(layerId);
        for (AccessGroup accessGroup : accessGroups) {
            AccessGroupLayer accesGroupLayer = new AccessGroupLayer();
            accesGroupLayer.setAccessGroup(accessGroup);
            accesGroupLayer.setLayer(layer);

            this.accessGroupLayerRepository.save(accesGroupLayer);
        }
    }

    /**
     *
     */
    public void unlinkAccessGroup(List<AccessGroup> accessGroups, Long layerId) {
        for (AccessGroup accessGroup : accessGroups) {
            List<AccessGroupLayer> list = this.accessGroupLayerRepository.listByAccessGroupLayerId(accessGroup.getId(), layerId);
            for (AccessGroupLayer accessGroupLayer : list) {
                this.accessGroupLayerRepository.delete(accessGroupLayer);
            }
        }
    }

    /**
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public ArrayList<AccessGroup> listAccessGroupByLayerId(Long id) {
        ArrayList<AccessGroup> grupos = new ArrayList<AccessGroup>();

        List<AccessGroupLayer> grupoAcessoCamadas = this.accessGroupLayerRepository.listByLayerId(id);

        for (AccessGroupLayer grupoAcessoCamada : grupoAcessoCamadas) {
            grupoAcessoCamada.setAccessGroup(this.accessGroupRepository.findById(grupoAcessoCamada.getAccessGroup().getId()));
            grupos.add(grupoAcessoCamada.getAccessGroup());
        }

        return grupos;
    }


    /**
     * @return
     */
    public List<String> listAllFeatures(List<String> listUrls) {
        this.template = new RestTemplate();
        List<String> listFeatures = new ArrayList<String>();

        for (String url : listUrls) {
            try {
                listFeatures.add(this.template.getForObject(url, String.class));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
        }

        return listFeatures;
    }


    /**
     *
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    private void removeLayersGroupPublishedEmpty(LayerGroup layerGroup) {
        if (layerGroup.getLayersGroup() != null) {
            List<LayerGroup> layersGroupExclusion = new ArrayList<LayerGroup>();

            for (LayerGroup layerGroupChild : layerGroup.getLayersGroup()) {
                removeLayersGroupPublishedEmpty(layerGroupChild);

                // remove o grupo de camada publicado superior vazio
                if (layerGroupChild.getLayersGroup().isEmpty() && layerGroupChild.getLayers().isEmpty()) {
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
    public List<Attribute> listAttributesByLayer(Long layerId) {

        return this.attributeRepository.listAttributeByLayer(layerId);
    }

    /**
     * Method that return a list of tools by user access group
     */
    public List<Tool> listToolsByUser() {
        List<Tool> toolsUser = new ArrayList<Tool>();
        final User user = ContextHolder.getAuthenticatedUser();

        //logado
        if (!user.equals(User.ANONYMOUS)) {
            List<AccessGroup> accessGroupsUser = this.accessGroupRepository.listByUser(user.getUsername());

            if (user.getRole() != UserRole.ADMINISTRATOR) {

                for (AccessGroup accessGroup : accessGroupsUser) {
                    accessGroup = this.accessGroupRepository.findOne(accessGroup.getId());

                    for (Tool tool : accessGroup.getTools()) {
                        toolsUser.add(tool);
                    }
                }

            } else {
                toolsUser = this.toolRepository.findAll();
            }

        } else {

            AccessGroup accessGroup = this.accessGroupRepository.findOne(AccessGroup.PUBLIC_GROUP_ID);

            accessGroup = this.accessGroupRepository.findOne(accessGroup.getId());

            for (Tool tool : accessGroup.getTools()) {
                toolsUser.add(tool);
            }

        }

        return toolsUser;
    }

    /**
     * Verifica se os grupos de acesso de uma página de grupos de acessos tem filhos
     *
     * @return
     */
    private Page<LayerGroup> hasChildren(Page<LayerGroup> layersGroups) {
        for (LayerGroup layerGroup : layersGroups) {
            layerGroup = this.hasChildren(layerGroup);
        }
        return layersGroups;
    }

    /**
     * Verifica se os grupos de acesso de uma lista de grupos de acessos tem filhos
     *
     * @return
     */
    private List<LayerGroup> hasChildren(List<LayerGroup> layersGroups) {
        for (LayerGroup layerGroup : layersGroups) {
            layerGroup = this.hasChildren(layerGroup);
        }
        return layersGroups;
    }

    /**
     * Verifica se o grupo de acesso tem filhos
     *
     * @return
     */
    private LayerGroup hasChildren(LayerGroup layerGroup) {

        List<LayerGroup> listLayerGroups = this.layerGroupRepository.listLayersGroupByLayerGroupId(layerGroup.getId());
        List<Layer> layers = this.layerRepository.listLayersByLayerGroupId(layerGroup.getId());
//		this.setIcon(layers);

        layerGroup.setLayers(layers);
        // Verifica se tem filhos, sejam layersGroups ou layers
        layerGroup.setHasChildren((listLayerGroups != null && listLayerGroups.size() > 0 || layers != null && layers.size() > 0)
                || ((listLayerGroups != null && listLayerGroups.size() > 0 && layers != null && layers.size() > 0)));

        if (layerGroup.getHasChildren()) {
            layerGroup.setLayersGroup(hasChildren(listLayerGroups));
        }
        return layerGroup;
    }


}
