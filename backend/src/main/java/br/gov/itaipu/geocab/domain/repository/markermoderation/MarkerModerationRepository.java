package br.gov.itaipu.geocab.domain.repository.markermoderation;

import br.gov.itaipu.geocab.domain.entity.markermoderation.MarkerModeration;
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
 * @since 12/01/2015
 */
public interface MarkerModerationRepository extends JpaRepository<MarkerModeration, Long> {
    /*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @return
     */
    @Query(value = "SELECT new MarkerModeration( markerModeration.id, markerModeration.created, markerModeration.status, marker) " +
            "FROM MarkerModeration markerModeration " +
            "LEFT OUTER JOIN markerModeration.marker marker " +
            "LEFT OUTER JOIN markerModeration.marker.layer layer " +
            "WHERE  ( ( LOWER(layer.name) LIKE '%' || LOWER(CAST(:filter AS string))  || '%' OR :filter = NULL ) ) ")
    Page<MarkerModeration> listByFilters(@Param("filter") String filter, Pageable pageable);

    /**
     * @return
     */
    @Query(value = "SELECT new MarkerModeration( markerModeration.id, markerModeration.created, markerModeration.status, marker) " +
            "FROM MarkerModeration markerModeration " +
            "LEFT OUTER JOIN markerModeration.marker marker " +
            "WHERE marker.id = :markerId")
    Page<MarkerModeration> listMarkerModerationByMarker(@Param("markerId") Long markerId, Pageable pageable);

    /**
     * @return
     */
    @Query(value = "SELECT new MarkerModeration( markerModeration.id, markerModeration.created, markerModeration.status, marker) " +
            "FROM MarkerModeration markerModeration " +
            "LEFT OUTER JOIN markerModeration.marker marker " +
            "WHERE marker.id = :markerId ORDER BY markerModeration.id DESC")
    List<MarkerModeration> listMarkerModerationByMarker(@Param("markerId") Long markerId);

}
