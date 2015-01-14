/**
 * 
 */
package br.com.geocab.domain.repository.marker;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author Administrador
 *
 */
public interface IMarkerRepository  extends IDataRepository<Marker, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/	
	/**
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT new Marker( marker.id, marker.status, marker.location, markerModeration.status, layer) " +
				"FROM Marker marker " +
				"LEFT OUTER JOIN marker.layer layer " +
				"LEFT OUTER JOIN marker.markerModeration markerModeration " +
				" ORDER BY markerModeration.status DESC"
				)
	public Page<Marker> listByFilters(  Pageable pageable );
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new Marker( marker.id, marker.status) " +
				"FROM Marker marker ")
	public List<Marker> listAll();
	
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer, user) " +
				"FROM Marker marker "+
				"LEFT OUTER JOIN marker.layer layer "+
				"LEFT OUTER JOIN marker.user user "+
				"WHERE (layer.id = :layerId AND (marker.status = 0 OR user.id = :userId) AND ( marker.deleted = NULL OR marker.deleted = FALSE ) ) ")
	public List<Marker> listMarkerByLayer(@Param("layerId") Long layerId, @Param("userId") Long userId);
	
	/**
	 * 
	 * @param layerId
	 * @param userId
	 * @return
	 */
	@Query(value="SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer, user) " +
				"FROM Marker marker "+
				"LEFT OUTER JOIN marker.layer layer "+
				"LEFT OUTER JOIN marker.user user "+ 
				"WHERE (layer.id = :layerId AND (marker.deleted = NULL OR marker.deleted = FALSE) )")
	public List<Marker> listMarkerByLayerAll(@Param("layerId") Long layerId);
	
}
