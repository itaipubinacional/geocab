package br.gov.itaipu.geocab.domain.repository.layer;

import java.util.List;

import br.gov.itaipu.geocab.domain.entity.layer.LayerGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category Repository
 * @since 22/09/2014
 */
public interface LayerGroupRepository extends JpaRepository<LayerGroup, Long> {

    /**
     * @param draftId
     * @return
     */
    LayerGroup findByDraftId(Long draftId);


    @Query(value = "SELECT new LayerGroup(layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroup.layerGroupUpper.id)"
            + "FROM LayerGroup layerGroup " +
            "WHERE ( layerGroup.id = :id ) ")
    LayerGroup findLayerGroupById(@Param("id") Long id);

    /**
     * @return
     */
    @Query(value = "SELECT new LayerGroup(layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroupUpper.id)"
            + "FROM LayerGroup layerGroup "
            + "LEFT OUTER JOIN layerGroup.layerGroupUpper layerGroupUpper "
            + "WHERE ( layerGroupUpper.id = null "
            + "AND layerGroup.draft = null "
            + "AND layerGroup.published = false ) "
            + "ORDER BY layerGroup.orderLayerGroup")
    List<LayerGroup> listLayersGroupUpper();


    /**
     * @return
     */
    @Query(value = "SELECT New LayerGroup ( layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroupUpper.id)"
            + "FROM LayerGroup layerGroup "
            + "LEFT OUTER JOIN layerGroup.layerGroupUpper layerGroupUpper "
            + "WHERE ( layerGroupUpper.id = :id "
            + "AND layerGroup.published = :published ) "
            + "ORDER BY layerGroup.orderLayerGroup")
    List<LayerGroup> listLayersGroupByLayerGroupId(@Param("id") Long id, @Param("published") Boolean published);

    /**
     * @param id
     * @return
     */
    @Query(value = "SELECT New LayerGroup ( layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroupUpper.id)"
            + "FROM LayerGroup layerGroup "
            + "LEFT OUTER JOIN layerGroup.layerGroupUpper layerGroupUpper "
            + "WHERE ( layerGroupUpper.id = :id ) "
            + "ORDER BY layerGroup.orderLayerGroup")
    List<LayerGroup> listLayersGroupByLayerGroupId(@Param("id") Long id);


    /**
     * @return
     */
    @Query(value = "FROM LayerGroup layerGroup "
            + "WHERE ( layerGroup.layerGroupUpper = NULL "
            + "AND layerGroup.published = true ) "
            + "ORDER BY orderLayerGroup")
    List<LayerGroup> listLayersGroupUpperPublished();

    /**
     * @return
     */
    @Query(value = "FROM LayerGroup layerGroup "
            + "WHERE ( layerGroup.published = true ) "
            + "ORDER BY orderLayerGroup")
    List<LayerGroup> listAllLayersGroupPublished();

    /**
     * @return
     */
    @Query(value = "FROM LayerGroup layerGroup "
            + "WHERE ( layerGroup.layerGroupUpper.id = :layerGroupUpperId "
            + "AND layerGroup.published = true ) "
            + "ORDER BY orderLayerGroup")
    List<LayerGroup> listLayersGroupPublishedChildren(@Param("layerGroupUpperId") Long layerGroupUpperId);

    /**
     * @return
     */
    @Query(value = "FROM LayerGroup layerGroup "
            + "WHERE ( layerGroup.layerGroupUpper = NULL "
            + "AND layerGroup.draft = null "
            + "AND layerGroup.published = false ) "
            + "ORDER BY ID DESC")
    public List<LayerGroup> listAllParentLayerGroup();

    /**
     * @return
     */
    @Query(value = "FROM LayerGroup layerGroup " +
            "WHERE ( LOWER(layerGroup.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) ")
    Page<LayerGroup> listByFilter(@Param("filter") String filter, Pageable pageable);

    /**
     * @return
     */
    @Query(value = "SELECT New LayerGroup ( layerGroup.id, layerGroup.name, layerGroup.orderLayerGroup, layerGroup.published, layerGroup.layerGroupUpper.id)"
            + "FROM LayerGroup layerGroup "
            + "WHERE ( layerGroup.layerGroupUpper = NULL "
            + "AND layerGroup.draft = null "
            + "AND layerGroup.published = false ) "
            + "AND layerGroup.id NOT IN (SELECT layer.layerGroup.id "
            + "FROM Layer layer "
            + "WHERE ( layer.name = :layerName ) AND ( layer.dataSource.id = :dataSourceId ) )"
            + "ORDER BY orderLayerGroup")
    List<LayerGroup> listSupervisorsFilter(@Param("layerName") String layerName, @Param("dataSourceId") Long dataSourceId);

}
