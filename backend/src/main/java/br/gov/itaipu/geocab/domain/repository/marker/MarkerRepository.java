/**
 *
 */
package br.gov.itaipu.geocab.domain.repository.marker;

import br.gov.itaipu.geocab.domain.entity.marker.Marker;
import br.gov.itaipu.geocab.domain.entity.marker.MarkerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;

/**
 * @author Administrador
 */
public interface MarkerRepository extends JpaRepository<Marker, Long> {

    /*-------------------------------------------------------------------
     *                           BEHAVIORS
     *-------------------------------------------------------------------*/

    /**
     * @return
     */
    @Query(value = "SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer, user) " +
            "FROM Marker marker " +
            "LEFT OUTER JOIN marker.layer layer " +
            "LEFT OUTER JOIN marker.user user " +
            "WHERE  ( ( LOWER(layer.title) LIKE '%' || LOWER(CAST(:layer AS string))  || '%' OR :layer = NULL )  " +
            "AND ( marker.status = :status OR :status = NULL ) " +
            "AND ( marker.created >= :dateStart OR CAST( :dateStart as date ) = NULL ) " +
            "AND ( marker.created <= :dateEnd OR CAST( :dateEnd as date ) = NULL  ) " +              // "AND ( marker.created <= :dateEnd OR CAST( :dateEnd as date ) = NULL  ) " +
            "AND ( LOWER(user.email) LIKE '%' || LOWER(CAST(:user AS string)) || '%' OR :user = NULL) ) " +
            "AND ( marker.deleted = NULL OR marker.deleted = FALSE )"
    )
    Page<Marker> listByFiltersWithoutOrder(@Param("layer") String layer, @Param("status") MarkerStatus status,
                                           @Param("dateStart") Calendar dateStart,
                                           @Param("dateEnd") Calendar dateEnd, @Param("user") String user,
                                           Pageable pageable);

    /**
     * @param pageable
     * @return
     */
    @Query(value = "SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer.id, layer.name, user, layer.publishedLayer.id) " +
            "FROM Marker marker " +
            "LEFT OUTER JOIN marker.layer layer " +
            "LEFT OUTER JOIN marker.user user " +
            "WHERE ( ( layer.id  = :layerId OR :layerId = NULL )" +
            "AND ( marker.status = :status OR :status = NULL ) " +
            "AND ( marker.created >= :dateStart OR CAST( :dateStart as date ) = NULL ) " +
            "AND ( marker.created <= :dateEnd OR CAST( :dateEnd as date ) = NULL  ) " +              // "AND ( marker.created <= :dateEnd OR CAST( :dateEnd as date ) = NULL  ) " +
            "AND ( LOWER(user.email) LIKE '%' || LOWER(CAST(:user AS string)) || '%' OR :user = NULL) ) " +
            "AND ( marker.deleted = NULL OR marker.deleted = FALSE ) order by marker.status"
    )
    Page<Marker> listByFilters(@Param("layerId") Long layerId, @Param("status") MarkerStatus status,
                               @Param("dateStart") Calendar dateStart, @Param("dateEnd") Calendar dateEnd,
                               @Param("user") String user, Pageable pageable);

    /**
     * @return
     */
    @Query(value = "SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer.id, layer.name, user, layer.publishedLayer.id) " +
            "FROM Marker marker " +
            "LEFT OUTER JOIN marker.layer layer " +
            "LEFT OUTER JOIN marker.user user " +
            "WHERE ( ( layer.id  = :layerId OR :layerId = NULL )" +
            "AND ( marker.status = :status OR :status = NULL ) " +
            "AND ( marker.created >= :dateStart OR CAST( :dateStart as date ) = NULL ) " +
            "AND ( marker.created <= :dateEnd OR CAST( :dateEnd as date ) = NULL  ) " +
            "AND ( LOWER(user.email) LIKE '%' || LOWER(CAST(:user AS string)) || '%' OR :user = NULL) ) " +
            "AND ( marker.deleted = NULL OR marker.deleted = FALSE )"
    )
    List<Marker> listByFiltersMap(@Param("layerId") Long layerId, @Param("status") MarkerStatus status,
                                  @Param("dateStart") Calendar dateStart, @Param("dateEnd") Calendar dateEnd,
                                  @Param("user") String user);


    /**
     * @return
     */
    @Query(value = "SELECT new Marker( marker.id, marker.status, marker.created, marker.location, layer, user) " +
            "FROM Marker marker " +
            "LEFT OUTER JOIN marker.layer layer " +
            "LEFT OUTER JOIN marker.user user " +
            "WHERE marker.id in (:ids) order by marker.status")
    Page<Marker> listByMarkers(@Param("ids") List<Long> ids, Pageable pageable);

    /**
     * @return
     */
    @Query(value = "SELECT new Marker( marker.id, marker.status) " +
            "FROM Marker marker ")
    List<Marker> listAll();


    /**
     * @return
     */
    @Query(value = "SELECT new Marker( marker.id, marker.status, marker.location, marker.created, layer.id, layer.name, layer.title, layer.icon, layer.published, layer.startEnabled, layer.startVisible, layer.enabled, user) " +
            "FROM Marker marker " +
            "LEFT OUTER JOIN marker.layer layer " +
            "LEFT OUTER JOIN marker.user user " +
            "WHERE (layer.id = :layerId AND (marker.status = 1 OR (user.id = :userId ) ) AND ( marker.deleted = NULL OR marker.deleted = FALSE ) ) ")
    List<Marker> listMarkerByLayer(@Param("layerId") Long layerId, @Param("userId") Long userId);

    /**
     * @param layerId
     * @return
     */
//    Long id, MarkerStatus status, Geometry location, Calendar created, Long layerId, String layerName, String layerTitle, String layerIcon, Boolean layerPublished, Boolean layerStartEnable, Boolean layerStartVisible, Boolean layerEnabled, User user
    @Query(value = "SELECT new Marker( marker.id, marker.status, marker.location, marker.created, layer.id, layer.name, layer.title, layer.icon, layer.published, layer.startEnabled, layer.startVisible, layer.enabled, user) " +
            "FROM Marker marker " +
            "LEFT OUTER JOIN marker.layer layer " +
            "LEFT OUTER JOIN marker.user user " +
            "WHERE (layer.id = :layerId AND (marker.deleted = NULL OR marker.deleted = FALSE) )")
    List<Marker> listMarkerByLayerAll(@Param("layerId") Long layerId);

    /**
     * @return
     */
    @Query(value = "SELECT new Marker( marker.id, marker.status, marker.location, marker.created, layer.id, layer.name, layer.title, layer.icon, layer.published, layer.startEnabled, layer.startVisible, layer.enabled, user) " +
            "FROM Marker marker " +
            "LEFT OUTER JOIN marker.layer layer " +
            "LEFT OUTER JOIN marker.user user " +
            "WHERE (layer.id = :layerId AND marker.status = 1 AND ( marker.deleted = NULL OR marker.deleted = FALSE ) ) ")
    List<Marker> listMarkerByLayerPublic(@Param("layerId") Long layerId);

    /**
     * @return
     */
    @Query(value = "SELECT new Marker( marker.id, marker.status, marker.location, marker.created, layer.id, layer.name, layer.title, layer.icon, layer.published, layer.startEnabled, layer.startVisible, layer.enabled, user) " +
            "FROM Marker marker WHERE (marker.id = :id) ")
    Marker findById(@Param("id") Long id);

}
