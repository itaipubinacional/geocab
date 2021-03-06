package br.com.geocab.domain.repository.layergroup;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.layer.LayerGroup;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 22/09/2014
 * @version 1.0
 * @category Repository
 *
 */
public interface ILayerGroupRepository extends IDataRepository<LayerGroup, Long>
{
	
	/**
	 * 
	 * @param draftId
	 * @return
	 */
	public LayerGroup findByDraftId( Long draftId );
	
	
	
	
	@Query(value="SELECT new LayerGroup(layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroup.layerGroupUpper.id)"
			+ "FROM LayerGroup layerGroup " +
			"WHERE ( layerGroup.id = :id ) " )
	public LayerGroup findLayerGroupById( @Param("id") Long id );
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new LayerGroup(layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroupUpper.id)"
			 + "FROM LayerGroup layerGroup "
			 + "LEFT OUTER JOIN layerGroup.layerGroupUpper layerGroupUpper " 
			 + "WHERE ( layerGroupUpper.id = null "
			 +	"AND layerGroup.draft = null "
			 +	"AND layerGroup.published = false ) "
			 + "ORDER BY layerGroup.orderLayerGroup" )
	public List<LayerGroup> listLayersGroupUpper();
	
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT New LayerGroup ( layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroupUpper.id)"
			+ "FROM LayerGroup layerGroup "
			+ "LEFT OUTER JOIN layerGroup.layerGroupUpper layerGroupUpper " 
			+ "WHERE ( layerGroupUpper.id = :id "
			+	"AND layerGroup.published = :published ) "
			+ "ORDER BY layerGroup.orderLayerGroup" )
	public List<LayerGroup> listLayersGroupByLayerGroupId( @Param("id") Long id, @Param("published") Boolean published );
		
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Query(value="SELECT New LayerGroup ( layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroupUpper.id)"
			+ "FROM LayerGroup layerGroup "
			+ "LEFT OUTER JOIN layerGroup.layerGroupUpper layerGroupUpper " 
			+ "WHERE ( layerGroupUpper.id = :id ) "
			+ "ORDER BY layerGroup.orderLayerGroup" )
	public List<LayerGroup> listLayersGroupByLayerGroupId( @Param("id") Long id );
	
	
	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.layerGroupUpper = NULL "
			+	"AND layerGroup.published = true ) "
			+ "ORDER BY orderLayerGroup" )
	public List<LayerGroup> listLayersGroupUpperPublished();

	/**
	 * 
	 * @return
	 */
//	@Query(value="SELECT new LayerGroup( layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroupUpper.id )  " +
//			"FROM LayerGroup layerGroup " +
//			"LEFT OUTER JOIN layerGroup.layerGroupUpper layerGroupUpper " +
//			"WHERE ( layerGroup.published = true ) " +
//			"ORDER BY layerGroup.orderLayerGroup")
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.published = true ) "
			+ "ORDER BY orderLayerGroup" )
	public List<LayerGroup> listAllLayersGroupPublished();
	
	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.layerGroupUpper.id = :layerGroupUpperId "
			+	"AND layerGroup.published = true ) "
			+ "ORDER BY orderLayerGroup" )
	public List<LayerGroup> listLayersGroupPublishedChildren( @Param("layerGroupUpperId") Long layerGroupUpperId );
	
	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.layerGroupUpper = NULL "
			+   "AND layerGroup.draft = null "
			+	"AND layerGroup.published = false ) "
			+ "ORDER BY ID DESC" )
	public List<LayerGroup> listAllParentLayerGroup();

	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " + 
			"WHERE ( LOWER(layerGroup.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " )
	public Page<LayerGroup> listByFilter( @Param("filter") String filter, Pageable pageable );
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT New LayerGroup ( layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroup.layerGroupUpper.id)" 
			+ "FROM LayerGroup layerGroup "
			+ "WHERE ( layerGroup.layerGroupUpper = NULL "
			+ "AND layerGroup.draft = null "
			+ "AND layerGroup.published = false ) "
			+ "AND layerGroup.id NOT IN (SELECT layer.layerGroup.id "
			+ "FROM Layer layer "
			+ "WHERE ( layer.name = :layerName ) AND ( layer.dataSource.id = :dataSourceId ) )"
			+ "ORDER BY orderLayerGroup" )
	public List<LayerGroup> listSupervisorsFilter( @Param("layerName") String layerName, @Param("dataSourceId") Long dataSourceId );
	
}
