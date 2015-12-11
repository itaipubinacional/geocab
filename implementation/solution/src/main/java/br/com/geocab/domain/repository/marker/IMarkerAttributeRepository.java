/**
 * 
 */
package br.com.geocab.domain.repository.marker;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.marker.MarkerAttribute;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author Administrador
 *
 */
public interface IMarkerAttributeRepository  extends IDataRepository<MarkerAttribute, Long>
{
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/	
	/**
	 * 
	 * @param markerId
	 * @return
	 */
	@Query(value="SELECT new MarkerAttribute( markerAttribute.id, markerAttribute.value, "
			+ " markerAttribute.marker.id, markerAttribute.marker.status, markerAttribute.marker.created, "
			+ "markerAttribute.marker.layer.id, markerAttribute.marker.layer.name, markerAttribute.marker.layer.title, "
			+ "markerAttribute.marker.user.id, markerAttribute.marker.user.name, markerAttribute.marker.user.email, markerAttribute.marker.user.enabled, "
			+ "markerAttribute.attribute.id, markerAttribute.attribute.name, markerAttribute.attribute.type, markerAttribute.attribute.required, markerAttribute.attribute.orderAttribute) " +
				 " FROM MarkerAttribute markerAttribute "+
				 " WHERE (markerAttribute.marker.id = :markerId)"+ 
				 " ORDER BY markerAttribute.id ASC")
	public List<MarkerAttribute> listAttributeByMarker( @Param("markerId") Long markerId );
	
	/**
	 * É utilizado somente no update LAYER
	 * 
	 * @param layerId
	 * @return
	 */
	@Query(value="SELECT new MarkerAttribute(  markerAttribute.id, markerAttribute.value, marker, attribute ) " +
				 " FROM MarkerAttribute markerAttribute "+  
				 " LEFT OUTER JOIN markerAttribute.marker marker " + 
				 " LEFT OUTER JOIN markerAttribute.attribute attribute " + 
				 " WHERE (attribute.id = :attributeId)")
	public List<MarkerAttribute> listMarkerAttributeByAttribute( @Param("attributeId") Long attributeId );
		
}
