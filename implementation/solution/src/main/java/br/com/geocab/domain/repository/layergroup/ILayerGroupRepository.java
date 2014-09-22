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
	
	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.layerGroupUpper = NULL "
			+	"AND grupoCamadas.draft = null "
			+	"AND grupoCamadas.published = false ) "
			+ "ORDER BY orderLayerGroup" )
	public List<LayerGroup> listLayerGroupUpper();
	
	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.layerGroupUpper = NULL "
			+	"AND layerGroup.published = true ) "
			+ "ORDER BY orderLayerGroup" )
	public List<LayerGroup> listLayerGroupUpperPublished();
	
	
	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.published = true ) "
			+ "ORDER BY orderLayerGroup" )
	public List<LayerGroup> listAllGruposCamadasPublicados();
	
	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.layerGroupUpper.id = :layerGroupUpperId "
			+	"AND grupoCamadas.published = true ) "
			+ "ORDER BY orderLayerGroup" )
	public List<LayerGroup> listGruposCamadasPublicadosFilhos( @Param("layerGroupUpperId") Long layerGroupUpperId );
	
	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " 
			+ "WHERE ( layerGroup.layerGroupUpper = NULL "
			+   "AND grupoCamadas.draft = null "
			+	"AND grupoCamadas.published = false ) "
			+ "ORDER BY ID DESC" )
	public List<LayerGroup> listAllParentLayerGroup();

	/**
	 * 
	 * @return
	 */
	@Query(value="FROM LayerGroup layerGroup " + 
			"WHERE ( LOWER(layerGroup.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) " )
	public Page<LayerGroup> listByFilter( @Param("filter") String filter, Pageable pageable );
}
