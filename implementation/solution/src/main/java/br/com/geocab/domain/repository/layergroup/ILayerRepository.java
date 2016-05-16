package br.com.geocab.domain.repository.layergroup;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.layer.Layer;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 22/09/2014
 * @version 1.0
 * @category Repository
 *
 */
public interface ILayerRepository extends IDataRepository<Layer, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/	
	/**
	 * 
	 * @param layerId
	 * @return
	 */
	@Query(value="SELECT new Layer(layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible," +
			" layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.published, dataSource, layerGroup.name, layerGroup.id, layer.publishedLayer.id)  " +
			"FROM Layer layer " +
			"LEFT OUTER JOIN layer.layerGroup layerGroup " +
			"LEFT OUTER JOIN layer.dataSource dataSource " + 
			"WHERE ( layer.id = :layerId ) " )
	public Layer findById( @Param("layerId") Long layerId);
	
	
	/**
	 * 
	 * @param filter
	 * @param dataSourceId
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT new Layer( layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, dataSource, layerGroup.name, layer.publishedLayer.id ) " +
				"FROM Layer layer " +
				"LEFT OUTER JOIN layer.dataSource dataSource " + 
				"LEFT OUTER JOIN layer.layerGroup layerGroup " +
				"WHERE ( ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
				"OR ( LOWER(layer.title) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
				"OR ( LOWER(dataSource.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " +
				"OR ( LOWER(layerGroup.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) ) " +
				"AND ( layer.dataSource.id = :dataSourceId OR :dataSourceId = NULL ) " +
				"AND ( layer.published = false )")
	public Page<Layer> listByFilters( @Param("filter") String filter, @Param("dataSourceId") Long dataSourceId, Pageable pageable );
	
	/**
	 * 
	 * @param idLayer
	 * @return
	 */
	@Query(value="SELECT new Layer(layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.enabled, layer.published, layer.dataSource, layer.publishedLayer.id )  " +
			"FROM Layer layer " +
			"WHERE ( layer.publishedLayer.layerGroup.id = :idLayer ) " +
			"ORDER BY layer.publishedLayer.orderLayer")
	public List<Layer> listLayersByLayerGroupPublished( @Param("idLayer") Long idLayer);
	
	/**
	 * 
	 * @param id
	 * @param published
	 * @return
	 */
	@Query(value="SELECT New Layer (layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.published, dataSource, layerGroup.name, layerGroup.id, publishedLayer.id)"
			+ " FROM Layer layer "
			+ " LEFT OUTER JOIN layer.dataSource dataSource "
			+ " LEFT OUTER JOIN layer.layerGroup layerGroup "
			+ " LEFT OUTER JOIN layer.publishedLayer publishedLayer "
			+ " WHERE ( layer.layerGroup.id = :id "
	        + " AND layer.layerGroup.published = :published ) "
	        + " ORDER BY layer.orderLayer" )
	public List<Layer> listLayersByLayerGroupId( @Param("id") Long id, @Param("published") Boolean published);

	/**
	 * 
	 * @param id
	 * @return
	 */
	@Query(value="SELECT New Layer (layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.published, dataSource, layerGroup.name, layerGroup.id, publishedLayer.id)"
			+ " FROM Layer layer "
			+ " LEFT OUTER JOIN layer.dataSource dataSource "
			+ " LEFT OUTER JOIN layer.layerGroup layerGroup "
			+ " LEFT OUTER JOIN layer.publishedLayer publishedLayer "  
			+ " WHERE ( layerGroup.id = :layerGroupId ) "
			+ " ORDER BY layer.orderLayer")
	public List<Layer> listLayersByLayerGroupId( @Param("layerGroupId") Long layerGroupId);
		
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new Layer(layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.enabled, layer.published, layer.dataSource, layer.publishedLayer.id) " +
			"FROM Layer layer " +
			"WHERE ( layer.publishedLayer != NULL "
			+ "AND layer.published = false ) ")
	public List<Layer> listLayersPublished();
	
	
	
	@Query(value="SELECT new Layer(layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.enabled, layer.published, layer.dataSource, layer.publishedLayer.id) " +
			"FROM Layer layer " +
			"WHERE ( layer.publishedLayer != NULL "
			+ "AND layer.startEnabled = true ) ")
	public List<Layer> listLayersStartEnable();
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new Layer(layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.enabled, layer.published, layer.layerGroup.id, layer.layerGroup.name, layer.layerGroup.orderLayerGroup, layer.publishedLayer.id) " 
			+ "FROM Layer layer "
			+ "WHERE ( layer.dataSource.url = NULL AND layer.publishedLayer != NULL AND layer.enabled = TRUE ) " )
	public List<Layer> listAllInternalLayerGroups();
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new Layer(layer.id, layer.title, layer.publishedLayer.id) "
			+ "FROM Layer layer " 
			+ "WHERE ( layer.dataSource.url = NULL ) " )
	public List<Layer> listAllInternalLayers();
	
	/**
	 * 
	 * @param layerName
	 * @param dataSourceId
	 * @return
	 */
    @Query(value="SELECT layer.layerGroup.id " +
            "FROM Layer layer " +
            "WHERE ( layer.name = :layerName) AND ( layer.dataSource.id = :dataSourceId )")
    public List<Long> listLayerGroupIdsByNameAndDataSource( @Param("layerName") String layerName, @Param("dataSourceId") Long dataSourceId);


}
