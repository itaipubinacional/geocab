/**
 * 
 */
package br.com.geocab.domain.repository.marker;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.datasource.DataSource;
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
	@Query(value="SELECT new Marker( marker.id, marker.latitude, marker.longitude, marker.status ) " +
				"FROM Marker marker ")
	public Page<Marker> listByFilters(  Pageable pageable );
	
	/**
	 * 
	 * @return
	 */
	@Query(value="SELECT new Marker( marker.id, marker.latitude, marker.longitude, marker.status ) " +
				"FROM Marker marker " )
	public List<Marker> listAll();
}
