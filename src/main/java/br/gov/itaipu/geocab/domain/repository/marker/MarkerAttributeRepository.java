/**
 *
 */
package br.gov.itaipu.geocab.domain.repository.marker;

import br.gov.itaipu.geocab.domain.entity.layer.AttributeType;
import br.gov.itaipu.geocab.domain.entity.marker.MarkerAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Administrador
 */
public interface MarkerAttributeRepository extends JpaRepository<MarkerAttribute, Long> {
    /*-------------------------------------------------------------------
     *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @param markerId
     * @return
     */
    @Query(value = "SELECT new MarkerAttribute( markerAttribute.id, markerAttribute.value, "
            + " markerAttribute.marker.id, markerAttribute.marker.status, markerAttribute.marker.created, "
            + "markerAttribute.marker.layer.id, markerAttribute.marker.layer.name, markerAttribute.marker.layer.title, "
            + "markerAttribute.marker.user.id, markerAttribute.marker.user.name, markerAttribute.marker.user.email, markerAttribute.marker.user.enabled, "
            + "markerAttribute.attribute.id, markerAttribute.attribute.name, markerAttribute.attribute.type, markerAttribute.attribute.required, markerAttribute.attribute.visible, markerAttribute.attribute.orderAttribute) " +
            " FROM MarkerAttribute markerAttribute " +
            " WHERE (markerAttribute.marker.id = :markerId)" +
            " ORDER BY markerAttribute.id ASC")
    List<MarkerAttribute> listAttributeByMarker(@Param("markerId") Long markerId);

    /**
     * É utilizado somente no update LAYER
     *
     * @return
     */
    @Query(value = "SELECT new MarkerAttribute(  markerAttribute.id, markerAttribute.value, marker, attribute ) " +
            " FROM MarkerAttribute markerAttribute " +
            " LEFT OUTER JOIN markerAttribute.marker marker " +
            " LEFT OUTER JOIN markerAttribute.attribute attribute " +
            " WHERE (attribute.id = :attributeId)")
    List<MarkerAttribute> listMarkerAttributeByAttribute(@Param("attributeId") Long attributeId);


    /**
     * @param attributeId
     * @param value
     * @param type
     * @return
     */
    @Query(value = "SELECT new MarkerAttribute(  markerAttribute.id, markerAttribute.value, "
            + "attribute.id, "
            + "marker.id, marker.location, marker.status, marker.deleted, marker.user, "
            + "layer.id, layer.name, layer.title, layer.icon, layer.startEnabled, layer.startVisible, layer.orderLayer, layer.minimumScaleMap, layer.maximumScaleMap, layer.enabled, layer.dataSource, layer.publishedLayer.id ) " +
            " FROM MarkerAttribute markerAttribute " +
            " LEFT OUTER JOIN markerAttribute.marker marker " +
            " LEFT OUTER JOIN markerAttribute.marker.layer layer " +
            " LEFT OUTER JOIN markerAttribute.attribute attribute " +
            " WHERE ((attribute.id = :attributeId) "
            + "AND ( LOWER(markerAttribute.value) LIKE '%' || LOWER(CAST(:value AS string))  || '%' OR :value IS NULL )  "
            + "AND ( LOWER(markerAttribute.attribute.name) LIKE '%' || LOWER(CAST(:name AS string))  || '%' OR :name IS NULL )  "
            + "AND ((markerAttribute.attribute.type = :type) OR :type IS NULL))")
    List<MarkerAttribute> listMarkerAttributeByAttributeIdAndFilters(@Param("attributeId") Long attributeId,
                                                                     @Param("name") String name,
                                                                     @Param("value") String value,
                                                                     @Param("type") AttributeType type);
}