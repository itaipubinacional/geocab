/**
 *
 */
package br.gov.itaipu.geocab.domain.repository.accessgroup;

import java.util.List;

import br.gov.itaipu.geocab.domain.entity.accessgroup.AccessGroupLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * @author Thiago Rossetto Afonso
 * @version 1.0
 * @category Repository
 * @since 03/12/2014
 */
public interface AccessGroupLayerRepository extends JpaRepository<AccessGroupLayer, Long> {
    /*-------------------------------------------------------------------
     *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param layer
     * @return
     */
    @Query(value = "SELECT new AccessGroupLayer( accessGroupLayer.id, accessGroupLayer.accessGroup.id, accessGroupLayer.layer.id ) " +
            "FROM AccessGroupLayer accessGroupLayer " +
            "WHERE  accessGroupLayer.layer.id = :layerId ")
    List<AccessGroupLayer> listByLayerId(@Param("layerId") Long layer);

    /**
     * @param groupId
     * @return
     */
    @Query(value = "SELECT new AccessGroupLayer( accessGroupLayer.id, accessGroupLayer.accessGroup.id, accessGroupLayer.layer.id ) " +
            "FROM AccessGroupLayer accessGroupLayer " +
            "WHERE accessGroupLayer.accessGroup.id = :groupId ")
    List<AccessGroupLayer> listByAccessGroupId(@Param("groupId") Long groupId);

    /**
     * @param groupId
     * @param layerId
     * @return
     */
    @Query(value = "SELECT new AccessGroupLayer( accessGroupLayer.id, accessGroupLayer.accessGroup.id, accessGroupLayer.layer.id ) " +
            "FROM AccessGroupLayer accessGroupLayer " +
            "WHERE  ((accessGroupLayer.accessGroup.id = :groupId) "
            + "AND (accessGroupLayer.layer.id = :layerId))")
    List<AccessGroupLayer> listByAccessGroupLayerId(@Param("groupId") Long groupId, @Param("layerId") Long layerId);
}
