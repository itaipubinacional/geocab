package br.gov.itaipu.geocab.domain.repository.markermoderation;


import br.gov.itaipu.geocab.domain.entity.markermoderation.Motive;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category Repository
 * @since 12/01/2015
 */
public interface MotiveRepository extends JpaRepository<Motive, Long> {
    /*-------------------------------------------------------------------
	 *				 		     BEHAVIORS
	 *-------------------------------------------------------------------*/

}
