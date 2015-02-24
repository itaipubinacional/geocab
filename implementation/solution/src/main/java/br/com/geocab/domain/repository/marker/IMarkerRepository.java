/**
 * 
 */
package br.com.geocab.domain.repository.marker;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.marker.Marker;
import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
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
	@Query(value="SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer, user) " +
				"FROM Marker marker " +
				"LEFT OUTER JOIN marker.layer layer " +
				"LEFT OUTER JOIN marker.user user " +
				"WHERE  ( ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:layer AS string))  || '%' OR :layer = NULL )  " +
				"AND ( marker.status = :status OR :status = NULL ) " +
				"AND ( marker.created >= :dateStart OR CAST( :dateStart as date ) = NULL ) " +
				"AND ( marker.created <= :dateEnd OR CAST( :dateEnd as date ) = NULL  ) " +
				"AND ( LOWER(user.email) LIKE '%' || LOWER(CAST(:user AS string)) || '%' OR :user = NULL) ) " +
				"AND ( marker.deleted = NULL OR marker.deleted = FALSE )"
				)
	public Page<Marker> listByFilters( @Param("layer") String layer, @Param("status") MarkerStatus status, @Param("dateStart") Calendar dateStart, @Param("dateEnd") Calendar dateEnd, @Param("user") String user, Pageable pageable );
	
	/**
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Query(value="SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer, user) " +
				"FROM Marker marker " +
				"LEFT OUTER JOIN marker.layer layer " +
				"LEFT OUTER JOIN marker.user user " +
				"WHERE  ( ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:layer AS string))  || '%' OR :layer = NULL )  " +
				"AND ( marker.status = :status OR :status = NULL ) " +
				"AND ( marker.created >= :dateStart OR CAST( :dateStart as date ) = NULL ) " +
				"AND ( marker.created <= :dateEnd OR CAST( :dateEnd as date ) = NULL  ) " +
				"AND ( LOWER(user.email) LIKE '%' || LOWER(CAST(:user AS string)) || '%' OR :user = NULL) ) " +
				"AND ( marker.deleted = NULL OR marker.deleted = FALSE )"
				)
	public List<Marker> listByFiltersMap( @Param("layer") String layer, @Param("status") MarkerStatus status, @Param("dateStart") Calendar dateStart, @Param("dateEnd") Calendar dateEnd, @Param("user") String user );
	
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer, user) " +
				"FROM Marker marker " +
				"LEFT OUTER JOIN marker.layer layer " +
				"LEFT OUTER JOIN marker.user user "+
				"WHERE marker.id in (:ids)" )
	public Page<Marker> listByMarkers(@Param("ids") List<Long> ids, Pageable pageable);
	
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
				"WHERE (layer.id = :layerId AND (marker.status = 1 OR (user.id = :userId AND marker.status != 2) ) AND ( marker.deleted = NULL OR marker.deleted = FALSE ) ) ")
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
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer, user) " +
				"FROM Marker marker "+
				"LEFT OUTER JOIN marker.layer layer "+
				"LEFT OUTER JOIN marker.user user "+
				"WHERE (layer.id = :layerId AND marker.status = 1 AND ( marker.deleted = NULL OR marker.deleted = FALSE ) ) ")
	public List<Marker> listMarkerByLayerPublic(@Param("layerId") Long layerId);
	
}
