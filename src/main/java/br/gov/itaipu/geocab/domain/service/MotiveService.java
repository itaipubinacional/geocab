/**
 *
 */
package br.gov.itaipu.geocab.domain.service;

import br.gov.itaipu.geocab.domain.entity.markermoderation.Motive;
import br.gov.itaipu.geocab.domain.repository.markermoderation.MotiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Vinicius Ramos Kawamoto
 * @version 1.0
 * @category Service
 * @since 12/01/2015
 */

@Service
@Transactional
public class MotiveService {
    /*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/

    /**
     *
     */
    @Autowired
    private MotiveRepository motiveRepository;
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * @return
     */
    @Transactional(readOnly = true)
    public List<Motive> listMotives() {
        return this.motiveRepository.findAll();
    }

}
