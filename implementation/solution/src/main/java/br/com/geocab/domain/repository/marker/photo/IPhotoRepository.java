/**
 * 
 */
package br.com.geocab.domain.repository.marker.photo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.geocab.domain.entity.marker.photo.Photo;
import br.com.geocab.infrastructure.jpa2.springdata.IDataRepository;

/**
 * @author emanuelvictor
 *
 */
public interface IPhotoRepository extends IDataRepository<Photo, Long>
{

	
	/*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	Photo findByIdentifier(String identifier);
	/**
	 * 
	 * @param path
	 * @return
	 */
	Page<Photo> findByIdentifierContaining(String path, Pageable pageable);
	
	/**
	 * @return
	 */
	@Query(value="SELECT photo " +
			 " FROM Photo photo WHERE (photo.photoAlbum.markerAttribute.marker.id = :markerId)"+ 
			 " ORDER BY photo.created DESC")
	List<Photo> listByMarkerId(@Param("markerId") Long markerId);
	
	
	@Query(value="SELECT photo "
			+ " FROM Photo photo"
			+ " WHERE (photo.photoAlbum.markerAttribute.marker.id = :markerId) ")
	Page<Photo> findPhotoByMarkerId(@Param("markerId") Long markerId, Pageable pageable);
	
	

	
}