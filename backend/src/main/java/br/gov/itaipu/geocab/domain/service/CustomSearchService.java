/**
 *
 */
package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.application.security.ContextHolder;
import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroup;
import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroupCustomSearch;
import br.gov.itaipu.geocab.domain.entity.configuration.account.User;
import br.gov.itaipu.geocab.domain.entity.configuration.account.UserRole;
import br.gov.itaipu.geocab.domain.entity.layer.CustomSearch;
import br.gov.itaipu.geocab.domain.entity.layer.Layer;
import br.gov.itaipu.geocab.domain.entity.layer.LayerField;
import br.gov.itaipu.geocab.domain.entity.marker.Marker;
import br.gov.itaipu.geocab.domain.entity.marker.MarkerAttribute;
import br.gov.itaipu.geocab.domain.repository.accessgroup.AccessGroupCustomSearchRepository;
import br.gov.itaipu.geocab.domain.repository.accessgroup.AccessGroupRepository;
import br.gov.itaipu.geocab.domain.repository.layer.AttributeRepository;
import br.gov.itaipu.geocab.domain.repository.layer.CustomSearchRepository;
import br.gov.itaipu.geocab.domain.repository.layer.LayerFieldRepository;
import br.gov.itaipu.geocab.domain.repository.layer.LayerRepository;
import br.gov.itaipu.geocab.domain.repository.marker.MarkerAttributeRepository;
import br.gov.itaipu.geocab.domain.repository.marker.MarkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Relevant class to control the actions of {@link CustomSearch}}
 *
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @category Service
 * @since 25/06/2014
 */

@Service
@Transactional
public class CustomSearchService {
    /*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
    /**
     *
     */
    // private static final Logger LOG = Logger.getLogger(
    // DataSourceService.class.getName() );
    /**
     * Repository of an {@link CustomSearch}
     */
    @Autowired
    private CustomSearchRepository customSearchRepository;

    /**
     *
     */
    @Autowired
    private LayerFieldRepository layerFieldRepository;

    /**
     *
     */
    @Autowired
    private AccessGroupCustomSearchRepository accessGroupCustomSearchRepository;

    /**
     *
     */
    @Autowired
    private AccessGroupRepository accessGroupRepository;

    /**
     *
     */
    @Autowired
    private MarkerAttributeRepository markerAttributeRepository;

    /**
     *
     */
    @Autowired
    private MarkerRepository markerRepository;

    /**
     *
     */
    @Autowired
    private AttributeRepository attributeRepository;
    /**
     *
     */
    @Autowired
    private AccessGroupCustomSearchRepository accessGroupCustomSearch;
    /**
     *
     */
    @Autowired
    private LayerRepository layerRepository;

	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * Remove os atributos do tipo foto da pesquisa personalizada, para que a
     * mesma possa ser salva com sucesso
     *
     * @param customSearch
     * @return
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    private static CustomSearch removeAttributesPhotoAlbum(
            CustomSearch customSearch) {
        for (Iterator<LayerField> layerFields = customSearch.getLayerFields()
                .iterator(); layerFields.hasNext(); ) {
            LayerField l = layerFields.next();
            if (l.getType() == null) {
                layerFields.remove();
            }
        }
        return customSearch;
    }

    /**
     * Method to insert an {@link CustomSearch}
     *
     * @return CustomSearch
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public Long insertCustomSearch(CustomSearch customSearch) {
        customSearch = this.customSearchRepository
                .save(removeAttributesPhotoAlbum(customSearch));
        for (AccessGroupCustomSearch accessGroupCustomSearch : customSearch
                .getAccessGroupCustomSearch()) {
            accessGroupCustomSearch.setCustomSearch(customSearch);
            this.accessGroupCustomSearchRepository
                    .save(accessGroupCustomSearch);
        }

        return customSearch.getId();
    }

    /**
     * Method to update an {@link CustomSearch}
     *
     * @param customSearch
     * @return
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public CustomSearch updateCustomSearch(CustomSearch customSearch) {
        customSearch.setLayer(this.customSearchRepository
                .getFindLayerById(customSearch.getLayer().getId()));
        customSearch.setAccessGroupCustomSearch(null);
        customSearch = this.customSearchRepository
                .save(removeAttributesPhotoAlbum(customSearch));

        return customSearch;
    }

    /**
     * Method to remove an {@link CustomSearch}
     *
     * @param id
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void removeCustomSearch(Long id) {
        List<AccessGroupCustomSearch> accessGroupCustomSearchs = this.accessGroupCustomSearchRepository
                .listByCustomSearchId(id);
        for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroupCustomSearchs) {
            this.accessGroupCustomSearchRepository
                    .delete(accessGroupCustomSearch);
        }

        this.customSearchRepository.delete(id);
    }

    /**
     * Method to find an {@link CustomSearch} by id
     *
     * @param id
     * @return customSearch
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    @Transactional(readOnly = true)
    public CustomSearch findCustomSearchById(Long id) {
        CustomSearch customSearch = this.customSearchRepository.findById(id);

        customSearch.setLayerFields(
                new HashSet<>(layerFieldRepository.findByCustomSearchId(id)));
        customSearch.setAccessGroupCustomSearch(new HashSet<>(
                accessGroupCustomSearchRepository.listByCustomSearchId(id)));

        // Get the legend of GeoServer's layer
        if (customSearch.getLayer().getDataSource().getUrl() != null) {
            int position = customSearch.getLayer().getDataSource().getUrl()
                    .lastIndexOf("ows?");
            String urlGeoserver = customSearch.getLayer().getDataSource()
                    .getUrl().substring(0, position);
            String urlLegend = urlGeoserver + Layer.LEGEND_GRAPHIC_URL
                    + customSearch.getLayer().getName()
                    + Layer.LEGEND_GRAPHIC_FORMAT;
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
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    @Transactional(readOnly = true)
    public Page<CustomSearch> listCustomSearchByFilters(String filter,
                                                        PageRequest pageable) {
        Page<CustomSearch> customsSearch = this.customSearchRepository
                .listByFilters(filter, pageable);

        for (CustomSearch customSearch : customsSearch.getContent()) {
            if (customSearch.getLayer().getDataSource().getUrl() != null) {
                int position = customSearch.getLayer().getDataSource().getUrl()
                        .lastIndexOf("ows?");
                String urlGeoserver = customSearch.getLayer().getDataSource()
                        .getUrl().substring(0, position);
                String urlLegend = urlGeoserver + Layer.LEGEND_GRAPHIC_URL
                        + customSearch.getLayer().getName()
                        + Layer.LEGEND_GRAPHIC_FORMAT;
                customSearch.getLayer().setLegend(urlLegend);
            }
        }

        return customsSearch;
    }

    /**
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    @Transactional(readOnly = true)
    public ArrayList<AccessGroup> listAccessGroupBySearchId(Long id) {
        ArrayList<AccessGroup> accessGroups = new ArrayList<AccessGroup>();

        List<AccessGroupCustomSearch> accessGroupCustomSearchs = this.accessGroupCustomSearchRepository
                .listByCustomSearchId(id);

        for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroupCustomSearchs) {
            accessGroups.add(accessGroupCustomSearch.getAccessGroup());
        }

        return accessGroups;
    }


    /**
     * Method that return an list of custom searchs according the access group
     * of user
     */
    @PreAuthorize("true")
    public List<CustomSearch> listCustomSearchsByUser() {

        if (ContextHolder.getAuthenticatedUser().getRole().equals(UserRole.ADMINISTRATOR)) {
            return this.customSearchRepository.findAll();
        }

        List<CustomSearch> customsSearchUser = new ArrayList<CustomSearch>();

        List<Layer> layers = layerRepository.listAllInternalLayerGroupsAndByAnonymousUser();

        List<AccessGroup> accessGroupUser = new ArrayList<>(Arrays.asList(this.accessGroupRepository.findOne(AccessGroup.PUBLIC_GROUP_ID)));

        // List of all access groups of user
        if (ContextHolder.getAuthenticatedUser() != null && !ContextHolder.getAuthenticatedUser().getRole().equals(UserRole.ANONYMOUS)) {
            layers = layerRepository.listAllInternalLayerGroupsAndByUser(ContextHolder.getAuthenticatedUser().getId());
            accessGroupUser = this.accessGroupRepository.listByUser(ContextHolder.getAuthenticatedUser().getEmail());
        }

        for (AccessGroup accessGroup : accessGroupUser) {
            accessGroup = this.accessGroupRepository.findById(accessGroup.getId());

            for (AccessGroupCustomSearch accessGroupCustomSearch : this.accessGroupCustomSearch.listByAccessGroupId(accessGroup.getId(), null)) {
                CustomSearch customSearch = customSearchRepository.findById(accessGroupCustomSearch.getCustomSearch().getId());

                for (Layer layer : layers) {
                    if (layer.getId().equals(customSearch.getLayer().getId())) {
                        accessGroupCustomSearch.setCustomSearch(customSearch);

                        accessGroupCustomSearch.getCustomSearch().setLayerFields(new HashSet<>(layerFieldRepository.findByCustomSearchId(accessGroupCustomSearch.getCustomSearch().getId())));

                        customsSearchUser.add(accessGroupCustomSearch.getCustomSearch());
                    }
                }
            }
        }
        return customsSearchUser;
    }

    /**
     * @param customSearchId (Survey List customized to be associated with the access
     *                       group).
     * @param accessGroups   (Access group to be associated).
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void linkAccessGroup(List<AccessGroup> accessGroups,
                                Long customSearchId) {
        CustomSearch customSearch = new CustomSearch(customSearchId);
        for (AccessGroup accessGroup : accessGroups) {
            AccessGroupCustomSearch accessGroupCustomSearch = new AccessGroupCustomSearch();
            accessGroupCustomSearch.setAccessGroup(accessGroup);
            accessGroupCustomSearch.setCustomSearch(customSearch);

            this.accessGroupCustomSearchRepository
                    .save(accessGroupCustomSearch);
        }
    }

    /**
     * @param accessGroups
     * @param customSearchId
     */
    @PreAuthorize("hasRole('" + UserRole.ADMINISTRATOR_VALUE + "')")
    public void unlinkAccessGroup(List<AccessGroup> accessGroups,
                                  Long customSearchId) {
        for (AccessGroup accessGroup : accessGroups) {
            List<AccessGroupCustomSearch> accessGroupCustomSearchs = this.accessGroupCustomSearchRepository
                    .listByAccessGroupCustomSearchId(accessGroup.getId(),
                            customSearchId);
            for (AccessGroupCustomSearch accessGroupCustomSearch : accessGroupCustomSearchs) {
                this.accessGroupCustomSearchRepository
                        .delete(accessGroupCustomSearch);
            }
        }
    }

    /**
     * Método utiliza para execução da pesquisa personalizada
     *
     * @param layerId
     * @param layerFields
     * @return
     */
    @Transactional(readOnly = true)
    public List<Marker> listMarkerByLayerFilters(Long layerId,
                                                 List<LayerField> layerFields) {

        final User user = ContextHolder.getAuthenticatedUser();

        List<Marker> listMarker = null;

        if (!user.equals(User.ANONYMOUS)) {

            if (user.getRole().name().equals(UserRole.ADMINISTRATOR_VALUE)
                    || user.getRole().name().equals(UserRole.MODERATOR_VALUE)) {
                listMarker = this.markerRepository
                        .listMarkerByLayerAll(layerId);
            } else {
                listMarker = this.markerRepository.listMarkerByLayer(layerId,
                        user.getId());
            }

        } else {
            listMarker = this.markerRepository.listMarkerByLayerPublic(layerId);
        }
        // Pega os markerAttributes do banco (Isso foi feito para não deixar a
        // requisição pesada)
        List<MarkerAttribute> markersAttribute = new ArrayList<>();
        // Variável auxiliar para verificar se há algum valor nas pesquisas
        boolean hasSearch = false;
        // Verifica se há algum valor na pesquisa
        for (LayerField layerField : layerFields) {
            if (layerField.getValue() != null
                    && layerField.getValue().length() > 0) {
                hasSearch = true;
                break;
            }
        }

        if (hasSearch) {
            for (LayerField layerField : layerFields) {
                if (layerField.getValue() != null
                        && layerField.getValue().length() > 0) {
                    markersAttribute.addAll(this.markerAttributeRepository
                            .listMarkerAttributeByAttributeIdAndFilters(
                                    layerField.getAttributeId(),
                                    layerField.getName(), layerField.getValue(),
                                    this.attributeRepository
                                            .findById(
                                                    layerField.getAttributeId())
                                            .getType()));
                }
            }
        } else {
            return listMarker;
        }

        for (MarkerAttribute markerAttribute : markersAttribute) {
            for (Marker marker : listMarker) {
                if (markerAttribute.getMarker().getId() == marker.getId()) {
                    markerAttribute.setMarker(marker);
                }
            }
        }

        // Popula a lista de markers com os markers attributos (Isso foi feito
        // para não deixar a requisição pesada)
        List<Marker> markersReturn = new ArrayList<>();
        for (MarkerAttribute markerAttribute : markersAttribute) {
            Marker marker = markerAttribute.getMarker();
            // Instancia lista de markerAttributes dentro do marker
            marker.setMarkerAttribute(new ArrayList<MarkerAttribute>());
            // Adiciona o markerAttribute
            marker.getMarkerAttribute().add(markerAttribute);
            // Adiciona o o marker na lista de retorno
            markersReturn.add(marker);
        }

        return markersReturn;
    }
}
