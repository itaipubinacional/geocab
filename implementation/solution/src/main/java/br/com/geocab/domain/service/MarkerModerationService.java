/**
 * 
 */
package br.com.geocab.domain.service;

import java.util.logging.Logger;

import java.util.List;

import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.geocab.domain.entity.marker.MarkerStatus;
import br.com.geocab.domain.entity.markermoderation.MarkerModeration;
import br.com.geocab.domain.repository.markermoderation.IMarkerModerationRepository;

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
@RemoteProxy(name="markerModerationService")
public class MarkerModerationService
{
	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	
	/**
	 * Log
	 */
	private static final Logger LOG = Logger.getLogger( MarkerModeration.class.getName() );
	
	/**
	 * 
	 */
	@Autowired
	private IMarkerModerationRepository markerModerationRepository;
	
	
	
	
	
	/*-------------------------------------------------------------------
	 *				 		    BEHAVIORS
	 *-------------------------------------------------------------------*/
	
	/**
	 *
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<MarkerModeration> listMarkerModerationByFilters( String filter, PageRequest pageable )
	{
		return this.markerModerationRepository.listByFilters(filter, pageable);
	}
	
	
	/**
	 * 
	 * Method to accept a {@link MarkerModeration}
	 * 
	 * @param markerModeration
	 * @return
	 */
	public MarkerModeration acceptMarkerModeration( MarkerModeration markerModeration )
	{			
		try
		{
			markerModeration.setStatus(MarkerStatus.ACCEPTED);
			markerModeration = this.markerModerationRepository.save(markerModeration);
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
		}
		
		return markerModeration;
	}
	
	/**
	 * 
	 * Method to refuse a {@link MarkerModeration}
	 * 
	 * @param markerModeration
	 * @return
	 */
	public MarkerModeration refuseMarkerModeration( MarkerModeration markerModeration )
	{			
		try
		{
			markerModeration.setStatus(MarkerStatus.REFUSED);
			markerModeration = this.markerModerationRepository.save(markerModeration);
		}
		catch ( DataIntegrityViolationException e )
		{
			LOG.info( e.getMessage() );
		}
		
		return markerModeration;
	}
	
	
	/**
	 *
	 * @param filter
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<MarkerModeration> listMarkerModerationByMarker( List<Long> ids, PageRequest pageable  )
	{
		return this.markerModerationRepository.listByMarker(ids, pageable);
	}
	
}
