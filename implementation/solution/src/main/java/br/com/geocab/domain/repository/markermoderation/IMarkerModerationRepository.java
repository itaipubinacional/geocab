package br.com.geocab.domain.repository.markermoderation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author Vinicius Ramos Kawamoto
 * @since 12/01/2015
 * @version 1.0
 * @category Repository
 *
 */
public interface IMarkerModerationRepository extends IDataRepository<MarkerModeration, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new MarkerModeration( markerModeration.id, markerModeration.status, marker) " +
				"FROM MarkerModeration markerModeration "+
				"LEFT OUTER JOIN markerModeration.marker marker " +
				"LEFT OUTER JOIN markerModeration.marker.layer layer " +
				"WHERE  ( ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) ) " )
	public Page<MarkerModeration> listByFilters(@Param("filter") String filter, Pageable pageable);
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new MarkerModeration( markerModeration.id, markerModeration.status, marker) " +
				"FROM MarkerModeration markerModeration "+
				"LEFT OUTER JOIN markerModeration.marker marker " +
				"WHERE marker.id in (:ids)" )
	public Page<MarkerModeration> listByMarker(@Param("ids") List<Long> ids, Pageable pageable);
}
