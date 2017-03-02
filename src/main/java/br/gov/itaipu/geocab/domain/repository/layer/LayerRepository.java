package br.gov.itaipu.geocab.domain.repository.layer;

import br.gov.itaipu.geocab.domain.entity.layer.Layer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category Repository
 * @since 22/09/2014
 */
public interface LayerRepository extends JpaRepository<Layer, Long> {
    /*-------------------------------------------------------------------
     *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param layerId
     * @return
     */
    @Query(value = "SELECT new Layer(layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible," +
            " layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.published, dataSource, layerGroup.name, layerGroup.id, layer.publishedLayer.id)  " +
            "FROM Layer layer " +
            "LEFT OUTER JOIN layer.layerGroup layerGroup " +
            "LEFT OUTER JOIN layer.dataSource dataSource " +
            "WHERE ( layer.id = :layerId ) ")
    Layer findById(@Param("layerId") Long layerId);


    /**
     * @param filter
     * @param dataSourceId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT new Layer( layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, dataSource, layerGroup.name, layer.publishedLayer.id ) " +
            "FROM Layer layer " +
            "LEFT OUTER JOIN layer.dataSource dataSource " +
            "LEFT OUTER JOIN layer.layerGroup layerGroup " +
            "WHERE ( ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
            "OR ( LOWER(layer.title) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
            "OR ( LOWER(dataSource.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
            "OR ( LOWER(layerGroup.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) ) " +
            "AND ( layer.dataSource.id = :dataSourceId OR :dataSourceId = NULL ) " +
            "AND ( layer.published = false )")
    Page<Layer> listByFilters(@Param("filter") String filter, @Param("dataSourceId") Long dataSourceId, Pageable pageable);


    /**
     * @param idLayer
     * @return
     */
    @Query(value = "SELECT New Layer (layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.published, dataSource, layerGroup.name, layerGroup.id, publishedLayer.id)"
            + " FROM Layer layer "
            + " LEFT OUTER JOIN layer.dataSource dataSource "
            + " LEFT OUTER JOIN layer.publishedLayer publishedLayer "
            + " LEFT OUTER JOIN publishedLayer.layerGroup layerGroup "
            + "WHERE ( layer.publishedLayer.layerGroup.id = :idLayer ) "
            + "ORDER BY layer.publishedLayer.orderLayer")
    List<Layer> listLayersByLayerGroupPublished(@Param("idLayer") Long idLayer);

    /**
     * @param id
     * @param published
     * @return
     */
    @Query(value = "SELECT New Layer (layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.published, dataSource, layerGroup.name, layerGroup.id, publishedLayer.id)"
            + " FROM Layer layer "
            + " LEFT OUTER JOIN layer.dataSource dataSource "
            + " LEFT OUTER JOIN layer.layerGroup layerGroup "
            + " LEFT OUTER JOIN layer.publishedLayer publishedLayer "
            + " WHERE ( layer.layerGroup.id = :id "
            + " AND layer.layerGroup.published = :published ) "
            + " ORDER BY layer.orderLayer")
    List<Layer> listLayersByLayerGroupId(@Param("id") Long id, @Param("published") Boolean published);

    /**
     * @return
     */
    @Query(value = "SELECT New Layer (layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.published, dataSource, layerGroup.name, layerGroup.id, publishedLayer.id)"
            + " FROM Layer layer "
            + " LEFT OUTER JOIN layer.dataSource dataSource "
            + " LEFT OUTER JOIN layer.layerGroup layerGroup "
            + " LEFT OUTER JOIN layer.publishedLayer publishedLayer "
            + " WHERE ( layerGroup.id = :layerGroupId ) "
            + " ORDER BY layer.orderLayer")
    List<Layer> listLayersByLayerGroupId(@Param("layerGroupId") Long layerGroupId);

    /**
     * @return
     */
    @Query(value = "SELECT new Layer(layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.enabled, layer.published, layer.dataSource, layer.publishedLayer.id) " +
            "FROM Layer layer " +
            "WHERE ( layer.publishedLayer != NULL "
            + "AND layer.published = false ) ")
    List<Layer> listLayersPublished();


    @Query(value = "SELECT new Layer(layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.enabled, layer.published, layer.dataSource, layer.publishedLayer.id) " +
            "FROM Layer layer " +
            "WHERE ( layer.publishedLayer != NULL "
            + "AND layer.startEnabled = true ) ")
    List<Layer> listLayersStartEnable();

    /**
     * @return
     */
    @Query(value = "SELECT DISTINCT new Layer( layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, dataSource, layerGroup.name, layer.publishedLayer.id) "
            + " FROM Layer layer "
            + " LEFT OUTER JOIN layer.dataSource dataSource "
            + " LEFT OUTER JOIN layer.layerGroup layerGroup "
            + "WHERE (layer.dataSource.url = NULL AND layer.publishedLayer != NULL AND layer.enabled = TRUE) ")
    List<Layer> listAllInternalLayerGroups();


    /**
     * @return
     */
    @Query(value = "SELECT new Layer(layer.id, layer.title, layer.publishedLayer.id) "
            + "FROM Layer layer "
            + "WHERE ( layer.dataSource.url = NULL ) ")
    List<Layer> listAllInternalLayers();

    /**
     * @param layerName
     * @param dataSourceId
     * @return
     */
    @Query(value = "SELECT layer.layerGroup.id " +
            "FROM Layer layer " +
            "WHERE ( layer.name = :layerName) AND ( layer.dataSource.id = :dataSourceId )")
    List<Long> listLayerGroupIdsByNameAndDataSource(@Param("layerName") String layerName, @Param("dataSourceId") Long dataSourceId);


    /**
     * @return
     */
    @Query(value = "SELECT New Layer (layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.published, dataSource, layerGroup.name, layerGroup.id, publishedLayer.id)"
            + " FROM Layer layer "
            + " LEFT OUTER JOIN layer.dataSource dataSource "
            + " LEFT OUTER JOIN layer.layerGroup layerGroup "
            + " LEFT OUTER JOIN layer.publishedLayer publishedLayer "
            + "WHERE ( publishedLayer.id = :publishedLayerId)")
    Layer listNotPublishedByPublishedId(@Param("publishedLayerId") Long publishedLayerId);

    //Serviços para restrição //TODO

    /**
     * @param userId
     * @return
     */
    @Query(value = "SELECT DISTINCT new Layer( layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, dataSource, layerGroup.name, layer.publishedLayer.id) "
            + " FROM Layer layer, AccessGroupLayer accessGroupLayer, AccessGroupUser accessGroupUser "
            + " LEFT OUTER JOIN layer.dataSource dataSource "
            + " LEFT OUTER JOIN layer.layerGroup layerGroup "
            + "WHERE (layer.dataSource.url = NULL AND layer.publishedLayer != NULL AND layer.enabled = TRUE) "
            + "AND ( (layer.id IN ("
            + "	SELECT accessGroupLayer.layer.id FROM accessGroupLayer.layer.id WHERE ( accessGroupUser.user.id = :userId AND accessGroupUser.accessGroup.id = accessGroupLayer.accessGroup.id )))) ")
    List<Layer> listAllInternalLayerGroupsAndByUser(@Param("userId") Long userId);

    /**
     * @return
     */
    @Query(value = "SELECT DISTINCT new Layer( layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, dataSource, layerGroup.name, layer.publishedLayer.id) "
            + " FROM Layer layer, AccessGroupLayer accessGroupLayer, AccessGroupUser accessGroupUser "
            + " LEFT OUTER JOIN layer.dataSource dataSource "
            + " LEFT OUTER JOIN layer.layerGroup layerGroup "
            + "WHERE (layer.dataSource.url = NULL AND layer.publishedLayer != NULL AND layer.enabled = TRUE) "
            + "AND ( (layer.id IN ("
            + "	SELECT accessGroupLayer.layer.id FROM accessGroupLayer.layer.id WHERE ( accessGroupLayer.accessGroup.id = 1 )))) ")
    List<Layer> listAllInternalLayerGroupsAndByAnonymousUser();

    /**
     * @param filter
     * @param dataSourceId
     * @param pageable
     * @return
     */
    @Query(value = "SELECT DISTINCT new Layer( layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, dataSource, layerGroup.name, layer.publishedLayer.id ) "
            + " FROM Layer layer, AccessGroupLayer accessGroupLayer, AccessGroupUser accessGroupUser "
            + " LEFT OUTER JOIN layer.dataSource dataSource "
            + " LEFT OUTER JOIN layer.layerGroup layerGroup "
            + "WHERE (( ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) "
            + "OR ( LOWER(layer.title) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) "
            + "OR ( LOWER(dataSource.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) "
            + "OR ( LOWER(layerGroup.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) ) "
            + "AND ( layer.dataSource.id = :dataSourceId OR :dataSourceId = NULL ) "
            + "AND ( layer.published = false ) ) "
            + "AND  ( layer.id IN ("
            + "	SELECT accessGroupLayer.layer.id FROM accessGroupLayer.layer.id WHERE (accessGroupUser.user.id = :userId AND accessGroupUser.accessGroup.id = accessGroupLayer.accessGroup.id  "
            + "  ))) ")
    Page<Layer> listByFiltersAndByUser(@Param("filter") String filter, @Param("dataSourceId") Long dataSourceId, @Param("userId") Long userId, Pageable pageable);


}
