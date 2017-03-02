/**
 * 
 */
package br.com.geocab.domain.repository.accessgroup;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.accessgroup.AccessGroupLayer;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * 
 * @author Thiago Rossetto Afonso
 * @since 03/12/2014
 * @version 1.0
 * @category Repository
 */
public interface IAccessGroupLayerRepository extends IDataRepository<AccessGroupLayer, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 * @param layer
	 * @return
	 */
	@Query(value="SELECT new AccessGroupLayer( accessGroupLayer.id, accessGroupLayer.accessGroup, accessGroupLayer.layer ) " +
				"FROM AccessGroupLayer accessGroupLayer " +
				"WHERE  accessGroupLayer.layer.id = :layerId " )
	public List<AccessGroupLayer> listByLayerId( @Param("layerId") Long layer );
	
	/**
	 * 
	 * @param groupId
	 * @return
	 */
	@Query(value="SELECT new AccessGroupLayer( accessGroupLayer.id, accessGroupLayer.accessGroup, accessGroupLayer.layer ) " +
			"FROM AccessGroupLayer accessGroupLayer " +
			"WHERE accessGroupLayer.accessGroup.id = :groupId " )
	public List<AccessGroupLayer> listByAccessGroupId( @Param("groupId") Long groupId );
	
	/**
	 * 
	 * @param groupId
	 * @param layerId
	 * @return
	 */
	@Query(value="SELECT new AccessGroupLayer( accessGroupLayer.id, accessGroupLayer.accessGroup, accessGroupLayer.layer ) " +
			"FROM AccessGroupLayer accessGroupLayer " +
			"WHERE  ((accessGroupLayer.accessGroup.id = :groupId) "
			+ "AND (accessGroupLayer.layer.id = :layerId))" )
	public List<AccessGroupLayer> listByAccessGroupLayerId( @Param("groupId") Long groupId, @Param("layerId") Long layerId );
}
