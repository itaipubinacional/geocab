/**
 *
 */
package br.gov.itaipu.geocab.domain.repository.marker.photo;


import br.gov.itaipu.geocab.domain.entity.marker.photo.PhotoAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author emanuelvictor
 */
public interface PhotoAlbumRepository extends JpaRepository<PhotoAlbum, Long> {

    /**
     * @param markerAttributeId
     * @return
     */
    PhotoAlbum findByMarkerAttributeId(Long markerAttributeId);
    /*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

}
