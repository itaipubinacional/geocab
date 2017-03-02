/**
 * 
 */
package br.com.geocab.domain.service;

import java.util.List;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.domain.entity.markermoderation.Motive;
import br.com.geocab.domain.entity.markermoderation.MotiveMarkerModeration;
import br.com.geocab.domain.repository.motive.IMotiveMarkerModerationRepository;
import br.com.geocab.domain.repository.motive.IMotiveRepository;

/**
 * 
 * @author Vinicius Ramos Kawamoto
 * @since 12/01/2015
 * @version 1.0
 * @category Service
 *
 */

@Service
@Transactional
@RemoteProxy(name="motiveService")
public class MotiveService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 */
	@Autowired
	private IMotiveRepository motiveRepository;
	
	/**
	 * 
	 */
	@Autowired
	private IMotiveMarkerModerationRepository motiveMarkerModerationRepository;
	
	
	
	
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 * 
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Motive> listMotives()
	{
		return this.motiveRepository.findAll();
	}
	
}
