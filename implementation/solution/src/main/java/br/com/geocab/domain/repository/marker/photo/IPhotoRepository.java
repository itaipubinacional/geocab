/**
 * 
 */
package br.com.geocab.domain.repository.marker.photo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

}