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
	@Query(value="SELECT new MarkerAttribute( markerAttribute.id, markerAttribute.value, marker, attribute ) " +
				 " FROM MarkerAttribute markerAttribute "+ 
				 " LEFT OUTER JOIN markerAttribute.marker marker " + 
				 " LEFT OUTER JOIN markerAttribute.attribute attribute " + 
				 " WHERE (markerAttribute.marker.id = :markerId)"+ 
				 " ORDER BY markerAttribute.id ASC")
	public List<MarkerAttribute> listAttributeByMarker( @Param("markerId") Long markerId );
	
	/**
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
